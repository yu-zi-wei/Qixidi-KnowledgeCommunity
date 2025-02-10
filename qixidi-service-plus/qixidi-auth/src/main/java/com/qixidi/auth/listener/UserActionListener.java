package com.qixidi.auth.listener;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.light.core.constant.Constants;
import com.light.redission.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zi-wei
 * @create 2025/2/7 15:10
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserActionListener implements SaTokenListener {

    private final SaTokenConfig tokenConfig;

    /**
     * 每次登录时触发
     */
    @Override
    public void doLogin(String loginType, Object loginId, String s1, SaLoginModel saLoginModel) {
        log.info("用户登录：【{}】【{}】【{}】", loginType, loginId, s1);
        log.info("saLoginModel:{}", saLoginModel);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(Constants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doLogout, userId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(Constants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doLogoutByLoginId, userId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(Constants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doReplaced, userId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 禁用
     *
     * @param s
     * @param o
     * @param s1
     * @param i
     * @param l
     */
    @Override
    public void doDisable(String s, Object o, String s1, int i, long l) {

    }

    /**
     * 解除禁用
     *
     * @param s
     * @param o
     * @param s1
     */
    @Override
    public void doUntieDisable(String s, Object o, String s1) {

    }

    @Override
    public void doOpenSafe(String s, String s1, String s2, long l) {

    }

    @Override
    public void doCloseSafe(String s, String s1, String s2) {

    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }

    @Override
    public void doRenewTimeout(String s, Object o, long l) {

    }

}
