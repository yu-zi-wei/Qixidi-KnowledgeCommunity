package com.qixidi.auth.helper;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ObjectUtil;
import com.light.core.constant.UserConstants;
import com.light.core.enums.DeviceType;
import com.light.core.enums.UserType;
import com.light.core.utils.StringUtils;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.model.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

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
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String TRIPARTITE_USER_KEY = "tripartiteUser";
    /**
     * 临时过期时间
     */
    @Value("${sa-token.activity-timeout}")
    private static Long ACTIVITY_TIMEOUT = 86400L;

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
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
        StpUtil.login(loginUser.getLoginId(), deviceType.getDevice());
        setLoginUser(loginUser);
    }

    public static void tripartiteLoginByDevice(TripartiteUser tripartiteUser, DeviceType deviceType) {
        StpUtil.login(tripartiteUser.getLoginId(), deviceType.getDevice());
        //登录
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
        return (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
    }

    public static TripartiteUser getTripartiteUserHelper() {
        return (TripartiteUser) StpUtil.getTokenSession().get(TRIPARTITE_USER_KEY);
    }

    /**
     * 获取前台用户(多级缓存)TripartiteUser
     */
    public static TripartiteUser getTripartiteUser() {
        TripartiteUser tripartiteUser = null;
        try {
            String uuid = StpUtil.getLoginIdAsString();
            if (ObjectUtil.isNull(uuid)) return null;
            SaSession tokenSession = StpUtil.getTokenSession();
            Map<String, Object> dataMap = tokenSession.getDataMap();
            Object value = dataMap.entrySet().stream().findFirst().get().getValue();
            if (ObjectUtil.isNull(value)) return null;
            tripartiteUser = (TripartiteUser) value;
        } catch (Exception e) {
            return null;
        }
        return tripartiteUser;
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

    public static String getTripartiteUuid() {
        String uuid = getTripartiteUser() == null ? null : getTripartiteUser().getUuid();
        return uuid;
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
