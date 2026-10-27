package com.qixidi.mcp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.light.core.enums.BusinessType;
import com.light.core.enums.MsgEnums;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.mcp.domain.bo.AgentKeyBo;
import com.qixidi.mcp.domain.vo.McpApiKeyVo;
import com.qixidi.mcp.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * agent API key 管理（前台，需登录；创建仅限创作者）。
 * 遵循项目惯例返回裸数据（含 void），由 ResponseAdvice 统一包装为 R——Controller 直接返回 R 会被再包一层
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agent/apiKey")
public class AgentApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * 创建 API key，明文仅本次响应返回一次
     */
    @Log(title = "agent API key", businessType = BusinessType.INSERT)
    @PostMapping
    public Map<String, String> create(@Validated @RequestBody AgentKeyBo bo) {
        TripartiteUser user = requireUser();
        // 与文章/小记发布同款创作者门槛
        if (!UserRoleEnums.getAdvancedRoleList().contains(user.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        String rawKey = apiKeyService.createKey(user, bo.getKeyName(), bo.getValidDays());
        return Map.of("apiKey", rawKey);
    }

    /**
     * 我的 key 列表（脱敏，不含哈希）
     */
    @GetMapping("/list")
    public List<McpApiKeyVo> list() {
        TripartiteUser user = requireUser();
        return apiKeyService.listByUser(user.getUuid()).stream()
            .map(k -> BeanUtil.copyProperties(k, McpApiKeyVo.class))
            .toList();
    }

    /**
     * 改名（启停预留，当前吊销即删行）
     */
    @Log(title = "agent API key", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public void rename(@PathVariable Long id, @Validated @RequestBody AgentKeyBo bo) {
        TripartiteUser user = requireUser();
        apiKeyService.renameKey(id, user.getUuid(), bo.getKeyName());
    }

    /**
     * 吊销（删行 + Sa-Token 会话登出，之后该 key 调 MCP 立即 401）
     */
    @Log(title = "agent API key", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public void revoke(@PathVariable Long id) {
        TripartiteUser user = requireUser();
        apiKeyService.revokeKey(id, user.getUuid());
    }

    private TripartiteUser requireUser() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录已过期");
        }
        return user;
    }
}
