package com.qixidi.mcp.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.light.exception.ServiceException;
import com.light.redission.utils.RedisUtils;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserTypeEnums;
import com.qixidi.mcp.domain.entity.AgentApiKey;
import com.qixidi.mcp.mapper.AgentApiKeyMapper;
import com.qixidi.mcp.utils.ApiKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * API key 签发与校验。
 *
 * 会话桥设计：签发 key 时以 API key 明文为自定义 tokenValue 创建 Sa-Token 长效会话
 * （device = "mcp-{id}"，与用户 web 会话隔离、互不影响）；校验通过后 Filter 把
 * X-API-Key 换头成 Authorization，业务侧 LoginHelper 体系原生可用。
 * 吊销 = 删行 + 删缓存 + 按 device 精确登出该 key 的会话（库中不落任何明文）。
 *
 * 过期模型：validDays 0=永久（expireTime 空、会话 timeout=-1）/ null=默认 365 / >0=N 天；
 * 校验缓存 TTL 1 天，命中免查库，未命中查库通过后重写——过期 key 最多多活一个缓存周期。
 *
 * @author zi-wei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private static final String CACHE_PREFIX = "mcp:apikey:";
    private static final long CACHE_TTL_DAYS = 1;
    private static final String DEVICE_PREFIX = "mcp-";
    /** 默认有效期（天） */
    public static final long DEFAULT_VALID_DAYS = 365;
    private static final long SECONDS_PER_DAY = 86400;

    private final AgentApiKeyMapper apiKeyMapper;

    /**
     * 签发 API key，返回明文（仅此一次）。
     * validDays：0=永久（expireTime 空、会话永不过期）；null=默认 365；>0=N 天
     */
    public String createKey(TripartiteUser user, String keyName, Long validDays) {
        boolean permanent = validDays != null && validDays == 0;
        long days = permanent ? 0 : (validDays == null || validDays < 0 ? DEFAULT_VALID_DAYS : validDays);
        String rawKey = ApiKeyGenerator.generate();
        AgentApiKey entity = new AgentApiKey();
        entity.setUserId(user.getUuid());
        entity.setKeyName(keyName);
        entity.setKeyHash(ApiKeyGenerator.sha256(rawKey));
        entity.setStatus(1);
        entity.setExpireTime(permanent ? null : new Date(System.currentTimeMillis() + days * SECONDS_PER_DAY * 1000));
        apiKeyMapper.insert(entity);

        // 创建 Sa-Token 长效会话：token 值 = API key 明文，device 按行 id 隔离；
        // timeout 与 expireTime 同天数对齐互为兜底，永久 key 传 -1（Sa-Token 永不过期语义）
        String device = DEVICE_PREFIX + entity.getId();
        StpUtil.login(user.getLoginId(), SaLoginParameter.create()
            .setDeviceType(device)
            .setToken(rawKey)
            .setTimeout(permanent ? -1 : days * SECONDS_PER_DAY)
            .setRightNowCreateTokenSession(true));
        // 用户信息写入该 token 的 session（key 与 LoginHelper.setTripartiteUser 一致，供读取）
        StpUtil.getTokenSessionByToken(rawKey).set(user.getLoginId(), user);
        return rawKey;
    }

    /**
     * 校验 API key 并返回绑定用户，失败抛 ServiceException
     */
    public TripartiteUser validate(String rawKey) {
        String hash = ApiKeyGenerator.sha256(rawKey);
        TripartiteUser cached = RedisUtils.getCacheObject(CACHE_PREFIX + hash);
        if (cached != null) {
            return cached;
        }
        AgentApiKey entity = apiKeyMapper.selectOne(
            Wrappers.<AgentApiKey>lambdaQuery().eq(AgentApiKey::getKeyHash, hash));
        if (entity == null || entity.getStatus() != 1
            || (entity.getExpireTime() != null && entity.getExpireTime().before(new Date()))) {
            throw new ServiceException("API key 无效或已过期");
        }
        Object loginId = StpUtil.getLoginIdByToken(rawKey);
        if (loginId == null) {
            throw new ServiceException("API key 会话已失效，请重新签发");
        }
        Object user = StpUtil.getTokenSessionByToken(rawKey).get(String.valueOf(loginId));
        if (!(user instanceof TripartiteUser tripartiteUser)) {
            throw new ServiceException("API key 用户信息缺失，请重新签发");
        }
        RedisUtils.setCacheObject(CACHE_PREFIX + hash, tripartiteUser, CACHE_TTL_DAYS, TimeUnit.DAYS);
        // 最后使用时间：尽力而为的观测字段。update(null, wrapper) 不触发实体自动填充——
        // 填充器依赖 Sa-Token 上下文，而本方法在 Filter 阶段执行（Sa 拦截器尚未初始化上下文），
        // 用 updateById 会抛"SaTokenContext 上下文尚未初始化"把认证拖成 401（Tomcat 线程复用时偶发）
        try {
            apiKeyMapper.update(null, Wrappers.<AgentApiKey>lambdaUpdate()
                .set(AgentApiKey::getLastUsedTime, new Date())
                .eq(AgentApiKey::getId, entity.getId()));
        } catch (Exception e) {
            log.warn("lastUsedTime 更新失败（不影响认证）: {}", e.getMessage());
        }
        return tripartiteUser;
    }

    /**
     * 用户自己的 key 列表
     */
    public List<AgentApiKey> listByUser(String uuid) {
        return apiKeyMapper.selectList(
            Wrappers.<AgentApiKey>lambdaQuery()
                .eq(AgentApiKey::getUserId, uuid)
                .orderByDesc(AgentApiKey::getId));
    }

    /**
     * 重命名
     */
    public void renameKey(Long id, String uuid, String keyName) {
        AgentApiKey entity = requireOwned(id, uuid);
        AgentApiKey upd = new AgentApiKey();
        upd.setId(entity.getId());
        upd.setKeyName(keyName);
        apiKeyMapper.updateById(upd);
    }

    /**
     * 吊销：删行 + 删缓存 + 登出该 key 的 Sa-Token 会话
     */
    public void revokeKey(Long id, String uuid) {
        AgentApiKey entity = requireOwned(id, uuid);
        apiKeyMapper.deleteById(entity.getId());
        RedisUtils.deleteObject(CACHE_PREFIX + entity.getKeyHash());
        StpUtil.logout(UserTypeEnums.TRIPARTITE_USER.getUserType() + ":" + uuid, DEVICE_PREFIX + entity.getId());
    }

    private AgentApiKey requireOwned(Long id, String uuid) {
        AgentApiKey entity = apiKeyMapper.selectById(id);
        if (entity == null || !entity.getUserId().equals(uuid)) {
            throw new ServiceException("API key 不存在");
        }
        return entity;
    }
}
