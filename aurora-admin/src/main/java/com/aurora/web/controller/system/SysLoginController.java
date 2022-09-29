package com.aurora.web.controller.system;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.constant.Constants;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.SysMenu;
import com.aurora.common.core.domain.entity.SysUser;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginBody;
import com.aurora.common.core.domain.model.LoginUserMain;
import com.aurora.common.core.domain.model.RegisterUserMain;
import com.aurora.common.core.domain.model.SmsLoginBody;
import com.aurora.common.enums.UserStatus;
import com.aurora.common.helper.LoginHelper;
import com.aurora.system.domain.vo.RouterVo;
import com.aurora.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthBaiduRequest;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.*;


/**
 * 登录验证
 *
 * @author Lion Li
 */
@Validated
@Api(value = "登录验证控制器", tags = {"登录验证管理"})
@RequiredArgsConstructor
@RestController
public class SysLoginController {

    @Value("${JustAuth.callBackBaseUrl}")
    private String callBackBaseUrl;
    @Value("${JustAuth.url}")
    private String url;
    @Value("${JustAuth.tripartiteUserType}")
    private String tripartiteUserType;
    private final SysLoginService loginService;
    private final ISysMenuService menuService;
    private final ITripartiteUserService iTripartiteUserService;
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
        // 生成令牌dddcdcd
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

    @ApiOperation("前台密码登录")
    @PostMapping("/oauth/front-desk/login")
    public R<Map<String, Object>> frontDeskLogin(@Validated @RequestBody LoginUserMain loginUserMain) {
        Map<String, Object> ajax = new HashMap<>();
        // 登录
        iTripartiteUserService.frontDeskLogin(loginUserMain);
        //返回token
        ajax.put(Constants.TOKEN, StpUtil.getTokenValue());
        return R.ok(ajax);
    }

    @ApiOperation("前台注册")
    @PostMapping("/oauth/front-desk/register")
    public R register(@Validated @RequestBody RegisterUserMain registerUserMain) {
        registerUserMain.setUserType(tripartiteUserType);
        return iTripartiteUserService.register(registerUserMain);
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
    @PostMapping("/logout")
    public R<Void> logout() {
        try {
            StpUtil.logout();
            loginService.logout(LoginHelper.getUsername());
        } catch (NotLoginException e) {
        }
        return R.ok("退出成功");
    }

    @ApiOperation("前台登出方法")
    @PostMapping("/oauth/logout")
    public R<Void> oauthLogout() {
        try {
            StpUtil.logout();
            loginService.logout(LoginHelper.getTripartiteUsername());
        } catch (NotLoginException e) {
        }
        return R.ok("退出成功");
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
        int index = uuidString.indexOf(":");
        String uuid = uuidString.substring(index + 1);
        String type = uuidString.substring(0, index);
        if (type.equals(tripartiteUserType)) {
            TripartiteUserVo tripartiteUser = iTripartiteUserService.queryById(uuid);
            Map<String, Object> ajax = new HashMap<>();
            Set<String> roles = new HashSet<>();
            ajax.put("user", tripartiteUser);
            ajax.put("roles", roles);
            ajax.put("permissions", roles);
            return R.ok(ajax);
        }
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

    /**
     * 第三方登录
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/oauth/render/{source}")
    public R renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        boolean login = StpUtil.isLogin();
        if (login) {
            return R.fail("请勿重复登录！");
        }
        AuthRequest authRequest = getAuthRequest(source);
        //生成授权url
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        //将这个url返回给前端Vue,由Vue去执行 授权页
        Map<String, String> map = new HashMap<>();
        map.put("url", authorizeUrl);
        return R.ok(map);
    }

    @RequestMapping("/oauth/callback/{source}")
    public Object login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        if (authResponse == null) {
            return R.fail(500, "授权失败");
        }
        TripartiteUser tripartiteUser = new TripartiteUser().setUuid(authResponse.getData().getUuid())
            .setUsername(authResponse.getData().getUsername())
            .setNickname(authResponse.getData().getNickname())
            .setAvatar(authResponse.getData().getAvatar())
            .setBlog(authResponse.getData().getBlog())
            .setCompany(authResponse.getData().getCompany())
            .setLocation(authResponse.getData().getLocation())
            .setEmail(authResponse.getData().getEmail())
            .setGender(authResponse.getData().getGender().getCode())
            .setRemark(authResponse.getData().getRemark())
            .setSource(authResponse.getData().getSource())
            .setUpdateTime(new Date())
            .setRoleId(UserStatus.GENERAL_USER.getLogCode())
            .setUserType(tripartiteUserType);
        iTripartiteUserService.oauthLogin(tripartiteUser);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        response.sendRedirect(url + "/index/transfer?key=" + authResponse.getData().getUuid() + "&token=" + saTokenInfo.getTokenValue());
        System.out.println("response:" + response);
        return authResponse;
    }

    /**
     * 根据具体的授权来源，获取授权请求工具类
     *
     * @param source
     * @return
     */
    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                    .clientId("c742ae98472b6f20f2d9abecc3b618adc39d974a8bea2583ff9113ef4689fc5d")
                    .clientSecret("f05d1afa78846da8e41a0b729a965fa1082cdff6fe2beb96875e4877314141b0")
                    .redirectUri(callBackBaseUrl + "/gitee")
                    .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                    .clientId("")
                    .clientSecret("")
                    .redirectUri(callBackBaseUrl + "/qq")
                    .build());
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                    .clientId("f7GYTxb23u4ijdxFbQykU0NI")
                    .clientSecret("89uK1SIUSLLjPpGXaxhebYQuh2PkbuFs")
                    .redirectUri(callBackBaseUrl + "/baidu")
                    .build());
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

    /**
     * 查询登录状态
     *
     * @return
     */
    @RequestMapping("/oauth/isLogin")
    public SaResult isLogin() {
        String loginIdAsString = StpUtil.getLoginIdAsString();
        if (loginIdAsString == null) {
            return null;
        }
        int index = loginIdAsString.indexOf(":");
        String after = loginIdAsString.substring(index + 1);
        return SaResult.ok().setData(iTripartiteUserService.isLogin());
    }
}
