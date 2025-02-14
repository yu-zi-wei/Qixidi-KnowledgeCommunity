package com.qixidi.auth.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.light.core.constant.UserConstants;
import com.light.core.enums.DeviceType;
import com.light.core.utils.StringUtils;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserType;
import com.qixidi.auth.domain.model.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
 * @author ziwei
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";

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
        //登录
        StpUtil.login(tripartiteUser.getLoginId(), deviceType.getDevice());
        //缓存信息
        setTripartiteUser(tripartiteUser);
    }

    /**
     * 设置用户数据(多级缓存)
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(StpUtil.getLoginIdAsString(), loginUser);
    }

    /**
     * 第三方登录（数据缓存）
     *
     * @param tripartiteUser
     */
    public static void setTripartiteUser(TripartiteUser tripartiteUser) {
        StpUtil.getTokenSession().set(StpUtil.getLoginIdAsString(), tripartiteUser);
    }

    /**
     * 获取后台用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) StpUtil.getTokenSession().get(StpUtil.getLoginIdAsString());
    }

    /**
     * 获取前台用户(多级缓存)TripartiteUser
     */
    public static TripartiteUser getTripartiteUser() {
        Object loginIdAsString = StpUtil.getLoginIdDefaultNull();//获取当前会话账号id, 如果未登录，则返回 null
        if (loginIdAsString == null) return null;
        return (TripartiteUser) StpUtil.getTokenSession().get(String.valueOf(loginIdAsString));
    }

    /**
     * 获取用户id（管理端）
     */
    public static Long getUserId() {
        String loginIdAsString = StpUtil.getLoginIdAsString();
        if (StringUtils.isBlank(loginIdAsString)) return null;
        String[] split = loginIdAsString.split(JOIN_CODE);
        String type = split[0];//获取类型
        if (type.equals(UserType.SYS_USER.getUserType())) {//前台用户
            LoginUser loginUser = getLoginUser();
            return loginUser.getUserId();
        }
        return null;

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
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) return null;
        return loginUser.getUsername();
    }

    /**
     * 获取前台用户账户
     */
    public static String getTripartiteUsername() {
        TripartiteUser tripartiteUser = getTripartiteUser();
        if (tripartiteUser == null) return null;
        return tripartiteUser.getUsername();
    }

    public static String getTripartiteUuid() {
        TripartiteUser tripartiteUser = getTripartiteUser();
        if (tripartiteUser == null) return null;
        return tripartiteUser.getUuid();
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
