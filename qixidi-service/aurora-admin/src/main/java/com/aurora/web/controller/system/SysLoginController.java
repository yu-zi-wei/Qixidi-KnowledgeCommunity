package com.aurora.web.controller.system;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.constant.Constants;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.SysMenu;
import com.aurora.common.core.domain.entity.SysUser;
import com.aurora.common.core.domain.model.LoginBody;
import com.aurora.common.core.domain.model.SmsLoginBody;
import com.aurora.common.helper.LoginHelper;
import com.aurora.system.domain.vo.RouterVo;
import com.aurora.system.service.ISysMenuService;
import com.aurora.system.service.ISysUserService;
import com.aurora.system.service.SysLoginService;
import com.aurora.system.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 登录验证
 *
 * @author Lion Li
 */
@Validated
@Api(value = "登录验证控制器", tags = {"登录验证管理"})
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
    @ApiOperation("登录方法")
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
    @ApiOperation("短信登录(示例)")
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
    @ApiOperation("获取用户信息")
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
    @ApiOperation("小程序登录(示例)")
    @PostMapping("/xcxLogin")
    public R<Map<String, Object>> xcxLogin(@NotBlank(message = "{xcx.code.not.blank}") String xcxCode) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.xcxLogin(xcxCode);
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

    @ApiOperation("后台登出方法")
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
    @ApiOperation("获取路由信息")
    @GetMapping("getRouters")
    public R<List<RouterVo>> getRouters() {
        Long userId = LoginHelper.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menuService.buildMenus(menus));
    }

}
