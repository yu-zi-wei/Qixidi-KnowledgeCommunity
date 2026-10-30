package com.qixidi.system.config;

import cn.dev33.satoken.stp.StpInterface;
import com.qixidi.auth.domain.enums.UserTypeEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.system.mapper.SysRoleMapper;
import com.qixidi.system.mapper.SysRolePermMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Sa-Token 自定义权限加载实现
 * 权限源：sys_user_role + sys_role_perm（权限码字典由 PermissionScanner 扫描代码注册，不落库）
 * 超管（userId=1）返回通配符 "*"，拥有全部权限
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    /**
     * Sa-Token 权限通配符
     */
    public static final String PERM_ALL = "*";

    private final SysRolePermMapper sysRolePermMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = resolveSysUserId(loginId);
        if (userId == null) {
            return Collections.emptyList();
        }
        if (LoginHelper.isAdmin(userId)) {
            return List.of(PERM_ALL);
        }
        List<String> perms = sysRolePermMapper.selectPermsByUserId(userId);
        return perms != null ? perms : Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = resolveSysUserId(loginId);
        if (userId == null) {
            return Collections.emptyList();
        }
        return sysRoleMapper.selectRolePermissionByUserId(userId)
                .stream()
                .map(role -> "role:" + role.getRoleId())
                .toList();
    }

    /**
     * 仅解析 sys_user 类型的 loginId，其他类型直接返回 null
     * loginId 格式：userType:id（如 sys_user:1 或 tripartite_user:uuid）
     */
    private Long resolveSysUserId(Object loginId) {
        String loginIdStr = String.valueOf(loginId);
        String[] parts = loginIdStr.split(LoginHelper.JOIN_CODE);
        if (parts.length < 2) {
            return null;
        }
        if (!UserTypeEnums.SYS_USER.getUserType().equals(parts[0])) {
            return null;
        }
        try {
            return Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            log.warn("解析后台用户ID失败: {}", loginIdStr);
            return null;
        }
    }
}
