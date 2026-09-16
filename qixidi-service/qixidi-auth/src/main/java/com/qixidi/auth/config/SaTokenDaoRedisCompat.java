package com.qixidi.auth.config;

import cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Sa-Token Redis dao 的跨版本兼容实现
 *
 * <p>官方 {@link SaTokenDaoForRedisTemplate#setStringAndKeepTTL} 通过
 * {@code Expiration.keepTtl()} 发出 {@code SET key val KEEPTTL XX}，存在两个环境坑：
 * <ul>
 *     <li>Redis &lt; 6.0 不支持 KEEPTTL 语法（本地开发环境为 Redis 3.2，报 ERR syntax error）</li>
 *     <li>Redisson 4.7.0 的 spring-data-redis 适配层不识别 keepTtl 哨兵值，
 *     下发 {@code PX -2000} 报 ERR invalid expire time in set（sa-token#970/#973，
 *     已通过 application.yml 排除 Redisson 自动装配、改走 Lettuce 规避）</li>
 * </ul>
 *
 * <p>本类改为显式读取 key 剩余 TTL 后用正数 PX 写入，等价复刻 KEEPTTL 语义，
 * 兼容所有 Redis 版本与连接实现：
 * <ul>
 *     <li>-2（key 不存在）：与原实现 {@code SetOption#ifPresent()}（XX）一致，不写入</li>
 *     <li>-1（永不过期）：无 TTL 写入</li>
 *     <li>正数：按剩余毫秒数写入</li>
 * </ul>
 *
 * <p>注意：官方 dao 的自动装配（{@code cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate}）无
 * {@code @ConditionalOnMissingBean} 守卫，已在 application.yml 的 {@code spring.autoconfigure.exclude}
 * 中排除，否则两个 SaTokenDao bean 注入歧义。
 *
 * @author ziwei
 */
@Component
public class SaTokenDaoRedisCompat extends SaTokenDaoForRedisTemplate {

    /**
     * Redis TTL 命令：key 不存在的返回值
     */
    private static final long TTL_KEY_NOT_EXIST = -2L;

    /**
     * Redis TTL 命令：key 永不过期的返回值
     */
    private static final long TTL_NEVER_EXPIRE = -1L;

    /**
     * 复刻 {@code SET key val KEEPTTL XX} 语义（入参 key 已是 wrapKey 后的最终 key，勿再包装）
     */
    @Override
    public void setStringAndKeepTTL(String key, String value) {
        Long ttlMillis = stringRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
        // getExpire 仅在管道/事务中返回 null，防御性按 key 不存在处理（与 XX 语义一致）
        if (ttlMillis == null || ttlMillis == TTL_KEY_NOT_EXIST) {
            return;
        }
        if (ttlMillis == TTL_NEVER_EXPIRE) {
            stringRedisTemplate.opsForValue().set(key, value);
        } else {
            stringRedisTemplate.opsForValue().set(key, value, ttlMillis, TimeUnit.MILLISECONDS);
        }
    }
}
