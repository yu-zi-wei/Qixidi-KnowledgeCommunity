package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.light.core.constant.UserConstants;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;

import com.qixidi.auth.domain.entity.SysRole;
import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.system.service.ISysRoleService;
import com.qixidi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysUserController {

    private final ISysUserService userService;
    private final ISysRoleService roleService;

    /**
     * 获取用户列表
     */
    @SaCheckPermission("user:list")
    @PostMapping("/list")
    public TableDataInfo<SysUser> list(@RequestBody SysUser user) {
        return userService.selectPageUserList(user, user);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @SaCheckPermission("user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public Map<String, Object> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> ajax = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (ObjectUtil.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put("user", sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            throw new ServiceException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        userService.insertUser(user);
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @SaCheckPermission("user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public void remove(@PathVariable Long[] userIds) {
        if (ArrayUtil.contains(userIds, LoginHelper.getUserId())) {
            throw new ServiceException("当前用户不能删除");
        }
        userService.deleteUserByIds(userIds);
    }

    /**
     * 重置密码
     */
    @SaCheckPermission("user:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public void resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        userService.resetPwd(user);
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public void changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.updateUserStatus(user);
    }

    /**
     * 根据用户编号获取授权角色
     */
    @SaCheckPermission("user:query")
    @GetMapping("/authRole/{userId}")
    public Map<String, Object> authRole(@PathVariable("userId") Long userId) {
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @SaCheckPermission("user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public void insertAuthRole(Long userId, Long[] roleIds) {
        userService.insertUserAuth(userId, roleIds);
    }
}
