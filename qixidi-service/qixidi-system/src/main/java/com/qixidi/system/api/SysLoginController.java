package com.qixidi.system.api;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.light.core.constant.Constants;
import com.light.redission.annotation.RepeatSubmit;

import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.auth.domain.model.LoginBody;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.system.service.ISysUserService;
import com.qixidi.system.service.SysLoginService;
import com.qixidi.system.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 登录验证管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysLoginController {
    private final SysLoginService loginService;
    private final ISysUserService userService;
    private final SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Map<String, Object> getInfo() {
        if (!StpUtil.isLogin()) return null;
        String uuidString = StpUtil.getLoginIdAsString();
        if (uuidString == null) return null;
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 登出
     *
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/logout")
    public void logout() {
        try {
            StpUtil.logout();
            loginService.logout(LoginHelper.getUsername());
        } catch (NotLoginException e) {
        }
    }

}
