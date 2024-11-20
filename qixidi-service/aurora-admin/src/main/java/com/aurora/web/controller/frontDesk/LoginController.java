package com.aurora.web.controller.frontDesk;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.constant.Constants;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginUserMain;
import com.aurora.common.core.domain.model.PhoneBinding;
import com.aurora.common.core.domain.model.RegisterUserMain;
import com.aurora.common.enums.UserStatus;
import com.aurora.common.helper.LoginHelper;
import com.aurora.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.*;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * @author ziwei
 * @date 2023年10月15日
 */
@Validated
@Api(value = "前台登录验证控制器", tags = {"前台登录验证管理"})
@RequiredArgsConstructor
@RestController
public class LoginController extends BaseController {
    @Value("${JustAuth.callBackBaseUrl}")
    private String callBackBaseUrl;
    @Value("${JustAuth.url}")
    private String url;
    @Value("${JustAuth.tripartiteUserType}")
    private String tripartiteUserType;
    private final SysLoginService loginService;
    private final ITripartiteUserService iTripartiteUserService;


    @ApiOperation("前台密码登录")
    @RepeatSubmit()
    @PostMapping("/oauth/front-desk/login")
    public R<Map<String, Object>> frontDeskLogin(@Validated @RequestBody LoginUserMain loginUserMain) {
        boolean login = StpUtil.isLogin();
        if (login) {
            return R.fail("请勿重复登录！");
        }
        Map<String, Object> ajax = new HashMap<>();
        // 登录
        iTripartiteUserService.frontDeskLogin(loginUserMain);
        String uuid = LoginHelper.getUserMapId().get("uuId");
        //返回token
        ajax.put(Constants.TOKEN, StpUtil.getTokenValue());
        ajax.put("uuid", uuid);
        return R.ok(ajax);
    }

    @ApiOperation("前台注册")
    @RepeatSubmit()
    @PostMapping("/oauth/front-desk/register")
    public R register(@Validated @RequestBody RegisterUserMain registerUserMain) {
        registerUserMain.setUserType(tripartiteUserType);
        return iTripartiteUserService.register(registerUserMain);
    }

    @ApiOperation("前台注销账号")
    @RepeatSubmit()
    @GetMapping("/oauth/account/cancellation")
    public R accountCancellation() {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) {
            return R.fail("注销失败！");
        }
        return toAjax(iTripartiteUserService.accountCancellation(uuid) ? 1 : 0);
    }


    @ApiOperation("发送邮箱验证码")
    @RepeatSubmit()
    @GetMapping("/oauth/front-desk/code/{email}/{mag}")
    public R sendOutCode(@NotNull(message = "邮箱不能为空") @PathVariable("email") String email,
                         @NotNull(message = "当前操作异常") @PathVariable("mag") String mag) {
        return iTripartiteUserService.sendOutCode(email, mag);
    }

    @ApiOperation("发送手机号验证码")
    @RepeatSubmit()
    @GetMapping("/oauth/phone/code/{phone}/{mag}")
    public R sendPhoneCode(@NotNull(message = "手机号不能为空") @PathVariable("phone") String phone,
                           @NotNull(message = "当前操作异常") @PathVariable("mag") String mag) throws Exception {
        return iTripartiteUserService.sendPhoneCode(phone, mag);
    }

    @ApiOperation("前台登出方法")
    @RepeatSubmit()
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
     * 第三方登录
     *
     * @param response
     * @throws IOException
     */
    @ApiOperation("第三方登录")
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

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/oauth/getInfo")
    public R<Map<String, Object>> getInfo() {
        Map<String, Object> ajax = new HashMap<>();
        if (!StpUtil.isLogin()) {
            ajax.put("isLogin", false);
            return R.ok(ajax);
        }
        String uuidString = StpUtil.getLoginIdAsString();
        if (uuidString == null) {
            ajax.put("isLogin", false);
            return R.ok(ajax);
        }
        int index = uuidString.indexOf(":");
        String uuid = uuidString.substring(index + 1);
        String type = uuidString.substring(0, index);
        TripartiteUserVo tripartiteUser = iTripartiteUserService.queryById(uuid);
        tripartiteUser.setUuid(uuid);
        ajax.put("user", tripartiteUser);
        Set<String> roles = new HashSet<>();
        ajax.put("roles", roles);
        ajax.put("permissions", roles);
        ajax.put("isLogin", true);
        return R.ok(ajax);
    }

    /**
     * 前台重置密码
     *
     * @throws IOException
     */
    @ApiOperation("前台重置密码")
    @RepeatSubmit()
    @PostMapping("/oauth/reset/password")
    public R resetPassword(@Validated @RequestBody RegisterUserMain registerUserMain) {
        return iTripartiteUserService.resetPassword(registerUserMain);
    }

    /**
     * 前台手机号绑定
     *
     * @throws IOException
     */
    @ApiOperation("前台手机号绑定")
    @RepeatSubmit()
    @PostMapping("/oauth/phone/binding")
    public R phoneNumberBinding(@Validated @RequestBody PhoneBinding phoneBinding) {
        return toAjax(iTripartiteUserService.phoneNumberBinding(phoneBinding) ? 1 : 0);
    }

    /**
     * 取消回调
     *
     * @param response
     * @param source
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    public void renderAuth(HttpServletResponse response, @PathVariable("source") String source) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * @param source   登陆类型回调地址
     * @param callback
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/oauth/callback/{source}")
    public Object login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse response) throws
        IOException {
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        if (authResponse == null) {
            return R.fail(500, "授权失败");
        }
        TripartiteUser tripartiteUser;
        tripartiteUser = new TripartiteUser().setUuid(authResponse.getData().getUuid())
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
        response.sendRedirect(url + "?key=" + authResponse.getData().getUuid()
            + "&token=" + saTokenInfo.getTokenValue());
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
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                    .clientId("929788922")
                    .clientSecret("33eb4edde7357a5a0688799da058f205")
                    .redirectUri(callBackBaseUrl + "/weibo")
                    .build());
                break;
            case "zhifubao":
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                    .clientId("2021003186671539")
                    .clientSecret("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCi/5GTP0zgbabIcIGp2ySiiwPX36k4Yn4IUw+ud4LS7hx2bLqTdM2e1bIzlYSdR8rJyUCgmD232plVQ2WQwNR1fXn1kuH8NyELE5/2mtncoe9e8lwwQvRrRH4MhzxUXFHjfYZeuO4M6/3LMGX5UDzn79qy0AGfcXIZ3Lagr+JBbsCEXoIni1JB4ZJ4dCwOrXcBfrKLDiXYjHgCWXvZQiGYd9FL/BCwMzNcwTurWeJZXQCqdUYC0Zkx+ipWBiarJhJFlauDr8DrvXwHZmqxBKKFz3ydubjtxZ6mj69sHDCT4rzJeAyU+NUYbXZSeYdGrsTui7UhouLVjRqq7PHmbJSJAgMBAAECggEAOkkfOkM5b0tmh2hLxXxahlElh60v6ry1iq6A/gc4XcrNnL9WnGH5ISITPgTqTf+dETkJBiRKdZMSn32hNPjvQDAlS15BtxnTlA1pdxp6az3wxcfulUdr9Wuhk0NMnHICylFZmkeQwEgnEeB1pog9r0aCssxUuDot7feRyrX6NyEakYwzSisMKneMj/wMJHqt5nsfmkdDaYQ+EJY3cS9R0wrKIocelB0NCAo6J5Yp3MGf2i2RGiejV2boz1gfWk5/xb3CekTyPL1HXMCqtExOH0ARHIjFVm2XfObJH4HRGUjFhxuHXIobvxw7IlyOeb8OL1mVcEv/9IMIdkdbQoNusQKBgQDuu/L6mbxIb0aLd/trcZXAglJMXxhrNqfwfx5pT990sHUYriYn99vnVVktRMQoLlK3ApKrGm1xn6m9Y4yRS2mpjkPtcTRXb3qhLDBsbACqtrUlYZgfdg+4iv34GMWCory86KehSiNd5GyyuxaV+HR28zH71w9rMwimwZMM2gQf3QKBgQCuyWfcYmeY0ikq/XM2bq3g7yGrxiPrGpU84gOGP5j6SWlnOwQM17fiSQXqF4lbRg94SrNHrqD75cqVn+9pR9usFKsm0gx2zhM0PBgULBkfdNkfHNMoJwy7upAFzJU2NzcehVZcrOy680pa70AQiJO6VLW/m0mWMbI5Utg2R42SnQKBgQCQv3R7bWszoARt27mdU6kwp9Ouaporp1/7+Zc+0ybxYWweIrkP0sCKar0/LUqh8jREslKrW8Kv+da49frsvYS7QX7+IH1T5ku8HUbe9j0RROZgW+QYb0To551FJ//ZTUKSZ537tS/sZetiEv2GwLYcySetQqAepzq/oO7ABBAsIQKBgE2RyssuzOUwl96hXqlQk1BZMCtIoxYq1QKRF5lTzjN9sw6CTXjZn50IZVyPl4DvZdwv4sK0SHOidrbSpPCnWGFVRuPSon9zU2iCJvmE3J1uQRGivD98nRUPZe187teBx69+42X7xI/vexdCSL9NraorAAIKC61hXsQ79QKZ3MWtAoGAHr6RRR9CUVEPNNeJYnOU514Ki+2dJNPdUKc36UNgJkfcgRY8t3F1jiFnFAAxJdjHf1gZtaw4xdvCDooppb1qEnGKnnomJHHzephEPWT+7bYHQyayLXQkHs8yhtRwGHGpwup9/jzvUl4ZnmxfpfpNmcHbKGc3iOwxfLrqFboIMPI=")
                    .alipayPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqYMVZNgofNXE8X5Fm0DcTkaOED3GNew7DDYjOPin6CrihAdAnLS4Vq8wFtlFGDx5JG4GkPLvm5qzByk84ZDxDVsuwco7ZKnzmH/C/J8EswpU/eDE71j90SOEEfo4b/3nTvrgwD3ueh2aDqXcvEwnINZzNAVvfsGuyxr7jgtEmXWNyzjEumIMYS3Y8JjZ4ArGD6NDRsif2wK3dsxcxqLnvVo/erLfJt6tuhV1go1fjjinJhGreZyJiA1i5/nMv8wziJ+WhxxBdLq4WX5yc4YqhAVo+Qt5wUvz7iLZrrOKxIOiIizLqivM6UHgHRTsaL0knF7O7n/dA5zpDrUk8Va5KwIDAQAB")
                    .redirectUri(callBackBaseUrl + "/zhifubao")
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
        return SaResult.ok().setData(iTripartiteUserService.isLogin());
    }
}
