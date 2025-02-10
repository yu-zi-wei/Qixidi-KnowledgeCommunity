package com.qixidi.system.controller;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.light.core.constant.Constants;
import com.light.core.core.domain.R;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.auth.domain.entity.SysMenu;
import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.auth.domain.model.LoginBody;
import com.qixidi.auth.domain.model.SmsLoginBody;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.system.domain.vo.RouterVo;
import com.qixidi.system.service.ISysMenuService;
import com.qixidi.system.service.ISysUserService;
import com.qixidi.system.service.SysLoginService;
import com.qixidi.system.service.SysPermissionService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
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
public class SysLoginController extends BaseController {
    private final SysLoginService loginService;
    private final ISysMenuService menuService;
    private final ISysUserService userService;
    private final SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 短信登录(示例)
     *
     * @param smsLoginBody 登录信息
     * @return 结果
     */
    @PostMapping("/smsLogin")
    public R<Map<String, Object>> smsLogin(@Validated @RequestBody SmsLoginBody smsLoginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.smsLogin(smsLoginBody.getPhonenumber(), smsLoginBody.getSmsCode());
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public R<Map<String, Object>> getInfo() {
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
        return R.ok(ajax);
    }

    /**
     * 小程序登录(示例)
     *
     * @param xcxCode 小程序code
     * @return 结果
     */
    @PostMapping("/xcxLogin")
    public R<Map<String, Object>> xcxLogin(@NotBlank(message = "{xcx.code.not.blank}") String xcxCode) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.xcxLogin(xcxCode);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    /**
     * 登出
     *
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/logout")
    public R<Void> logout() {
        try {
            StpUtil.logout();
            loginService.logout(LoginHelper.getUsername());
        } catch (NotLoginException e) {
        }
        return R.ok("退出成功");
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public R<List<RouterVo>> getRouters() {
        Long userId = LoginHelper.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menuService.buildMenus(menus));
    }

}
