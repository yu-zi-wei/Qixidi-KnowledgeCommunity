package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.constant.UserConstants;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.auth.domain.entity.SysRole;
import com.qixidi.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
@LogModule("角色管理")
public class SysRoleController {

    private final ISysRoleService roleService;

    /**
     * 查询角色信息列表
     */
    @SaCheckPermission("role:list")
    @PostMapping("/list")
    public TableDataInfo<SysRole> list(@RequestBody SysRole role) {
        return roleService.selectPageRoleList(role, role);
    }

    /**
     * 根据角色编号获取详细信息（含已分配权限码）
     */
    @SaCheckPermission("role:query")
    @GetMapping(value = "/{roleId}")
    public SysRole getInfo(@PathVariable Long roleId) {
        return roleService.selectRoleById(roleId);
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("role:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            throw new ServiceException("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        roleService.insertRole(role);

    }

    /**
     * 修改保存角色（含权限码重写）
     */
    @SaCheckPermission("role:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            throw new ServiceException("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            throw new ServiceException("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }

        roleService.updateRole(role);
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("role:edit")
    @Log(title = "修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public void changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.updateRoleStatus(role);
    }

    /**
     * 删除角色
     */
    @SaCheckPermission("role:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public void remove(@PathVariable Long[] roleIds) {
        roleService.deleteRoleByIds(roleIds);
    }

    /**
     * 获取角色选择框列表
     */
    @SaCheckPermission("role:query")
    @GetMapping("/optionselect")
    public List<SysRole> optionselect() {
        return roleService.selectRoleAll();
    }
}
