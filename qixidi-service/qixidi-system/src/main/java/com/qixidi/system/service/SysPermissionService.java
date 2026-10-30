package com.qixidi.system.service;

import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.system.config.StpInterfaceImpl;
import com.qixidi.system.mapper.SysRolePermMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理（/getInfo 数据源）
 *
 * @author ziwei
 */
@RequiredArgsConstructor
@Service
public class SysPermissionService {

    private final ISysRoleService roleService;
    private final SysRolePermMapper rolePermMapper;

    /**
     * 获取用户角色标识集合（roleKey）
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取用户权限码集合（sys_role_perm，超管通配）
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add(StpInterfaceImpl.PERM_ALL);
        } else {
            perms.addAll(rolePermMapper.selectPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}
