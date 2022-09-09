package com.aurora.common.helper;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurora.common.constant.UserConstants;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginUser;
import com.aurora.common.enums.DeviceType;
import com.aurora.common.enums.UserType;
import com.aurora.common.exception.UtilException;
import com.aurora.common.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String TRIPARTITE_USER_KEY = "tripartiteUser";

    private static final ThreadLocal<LoginUser> LOGIN_CACHE = new ThreadLocal<>();
    private static final ThreadLocal<TripartiteUser> TRIPARTITE_USER_CACHE = new ThreadLocal<>();

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        LOGIN_CACHE.set(loginUser);
        StpUtil.login(loginUser.getLoginId());
        setLoginUser(loginUser);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        LOGIN_CACHE.set(loginUser);
        StpUtil.login(loginUser.getLoginId(), deviceType.getDevice());
        setLoginUser(loginUser);
    }

    public static void tripartiteLoginByDevice(TripartiteUser tripartiteUser, DeviceType deviceType) {
        TRIPARTITE_USER_CACHE.set(tripartiteUser);
        //登录
        String loginId = tripartiteUser.getLoginId();
        StpUtil.login(loginId, deviceType.getDevice());
        setTripartiteUser(tripartiteUser);
    }

    /**
     * 设置用户数据(多级缓存)
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 第三方登录（数据缓存）
     *
     * @param tripartiteUser
     */
    public static void setTripartiteUser(TripartiteUser tripartiteUser) {
        StpUtil.getTokenSession().set(TRIPARTITE_USER_KEY, tripartiteUser);
    }

    /**
     * 获取后台用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = LOGIN_CACHE.get();
        if (loginUser != null) {
            return loginUser;
        }
        return (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
    }

    /**
     * 获取前台用户(多级缓存)TripartiteUser
     */
    public static TripartiteUser getTripartiteUser() {
        TripartiteUser tripartiteUser = TRIPARTITE_USER_CACHE.get();
        if (tripartiteUser != null) {
            return tripartiteUser;
        }
        return (TripartiteUser) StpUtil.getTokenSession().get(TRIPARTITE_USER_KEY);
    }

    /**
     * 清除一级缓存 防止内存问题
     */
    public static void clearCache() {
        LOGIN_CACHE.remove();
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String userId = null;
            for (UserType value : UserType.values()) {
                if (StringUtils.contains(loginId, value.getUserType())) {
                    String[] strs = StringUtils.split(loginId, JOIN_CODE);
                    // 用户id在总是在最后
                    userId = strs[strs.length - 1];
                }
            }
            if (StringUtils.isBlank(userId)) {
                throw new UtilException("登录用户: LoginId异常 => " + loginId);
            }
            return Long.parseLong(userId);
        }
        return loginUser.getUserId();
    }

    public static Map<String, String> getUserMapId() {
        Map<String, String> map = new HashMap();
        TripartiteUser tripartiteUser = getTripartiteUser();
        if (ObjectUtil.isNull(tripartiteUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String uuId = null;
            String userType = null;
            for (UserType value : UserType.values()) {
                if (StringUtils.contains(loginId, value.getUserType())) {
                    String[] strs = StringUtils.split(loginId, JOIN_CODE);
                    // 用户id在总是在最后
                    uuId = strs[strs.length - 1];
                    userType = strs[strs.length - 2];
                }
            }
            if (StringUtils.isBlank(uuId)) {
                throw new UtilException("登录用户: LoginId异常 => " + loginId);
            }
            map.put("uuId", uuId);
            map.put("userType", userType);
            return map;
        }
        map.put("uuId", tripartiteUser.getUuid());
        map.put("userType", tripartiteUser.getUserType());
        return map;
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取后台用户账户
     */
    public static String getUsername() {
        return getLoginUser() == null ? null : getLoginUser().getUsername();
    }

    /**
     * 获取前台用户账户
     */
    public static String getTripartiteUsername() {
        return getTripartiteUser() == null ? null : getTripartiteUser().getUsername();
    }

    /**
     * 获取用户类型
     */
    public static UserType getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserType.getUserType(loginId);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

}
