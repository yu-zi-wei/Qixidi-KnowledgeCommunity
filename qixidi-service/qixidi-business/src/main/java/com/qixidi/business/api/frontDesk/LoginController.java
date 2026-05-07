package com.qixidi.business.api.frontDesk;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.light.core.config.justAuth.JustAuthConfig;
import com.light.core.constant.Constants;
import com.light.exception.ServiceException;
import com.light.redission.annotation.RepeatSubmit;
import com.light.webSocket.utils.WebSocketUtils;

import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.model.LoginUserMain;
import com.qixidi.auth.domain.model.PhoneBinding;
import com.qixidi.auth.domain.model.RegisterUserMain;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.domain.vo.user.UserLoginStatusVo;
import com.qixidi.business.domain.vo.user.UserSimpleInfoVo;
import com.qixidi.business.service.ITripartiteUserService;
import com.qixidi.system.service.SysLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 【前台】登录验证管理
 *
 * @author ziwei
 * @date 2023年10月15日
 */
@Validated
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final JustAuthConfig justAuthConfig;
    private final SysLoginService loginService;
    private final ITripartiteUserService iTripartiteUserService;

    /**
     * 密码登录
     *
     * @param loginUserMain
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/oauth/frontDesk/login")
    public Map<String, Object> frontDeskLogin(@Validated @RequestBody LoginUserMain loginUserMain) {
        boolean login = StpUtil.isLogin();
        if (login) throw new ServiceException("请勿重复登录");
        Map<String, Object> ajax = new HashMap<>();
        // 登录
        iTripartiteUserService.frontDeskLogin(loginUserMain);
        String uuid = LoginHelper.getTripartiteUuid();
        //返回token
        ajax.put(Constants.TOKEN, StpUtil.getTokenValue());
        ajax.put("uuid", uuid);
        return ajax;
    }

    /**
     * 注册
     *
     * @param registerUserMain
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/oauth/frontDesk/register")
    public void register(@Validated @RequestBody RegisterUserMain registerUserMain) {
        registerUserMain.setUserType(justAuthConfig.getTripartiteUserType());
        iTripartiteUserService. register(registerUserMain);
    }

    /**
     * 注销账号
     *
     * @return
     */
    @RepeatSubmit()
    @GetMapping("/oauth/account/cancellation")
    public void accountCancellation() {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) throw new ServiceException("注销失败");
        iTripartiteUserService.accountCancellation(uuid);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param type
     * @return
     */
    @RepeatSubmit()
    @GetMapping("/oauth/email/code/{email}/{type}")
    public void sendOutCode(@PathVariable("email") String email, @PathVariable("type") Integer type) {
        iTripartiteUserService.sendOutCode(email, type);
    }

    /**
     * 发送手机号验证码
     *
     * @param phone
     * @param type
     * @param request
     * @return
     * @throws Exception
     */
    @RepeatSubmit()
    @GetMapping("/oauth/phone/code/{phone}/{type}")
    public void sendPhoneCode(@PathVariable("phone") String phone, @PathVariable("type") Integer type, HttpServletRequest request) throws Exception {
        iTripartiteUserService.sendPhoneCode(phone, type, request);
    }

    /**
     * 登出
     *
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/oauth/logout")
    public void oauthLogout() {
        try {
            String tripartiteUuid = LoginHelper.getTripartiteUuid();
            if (tripartiteUuid != null) {
                //断开WebSocket连接
                WebSocketUtils.removeLinks(tripartiteUuid);
                StpUtil.logout();
                loginService.logout(LoginHelper.getTripartiteUsername());
            }
        } catch (NotLoginException e) {
        }
    }

    /**
     * 第三方登录
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/oauth/render/{source}")
    public Map<String, String> renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        boolean login = StpUtil.isLogin();
        if (login) throw new ServiceException("请勿重复登录");
        AuthRequest authRequest = iTripartiteUserService.getAuthRequest(source);
        //生成授权url
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        //将这个url返回给前端Vue,由Vue去执行 授权页
        Map<String, String> map = new HashMap<>();
        map.put("url", authorizeUrl);
        return map;
    }


    /**
     * 登录回调取消
     *
     * @param response
     * @param source
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    public void renderAuth(HttpServletResponse response, @PathVariable("source") String source) throws IOException {
        AuthRequest authRequest = iTripartiteUserService.getAuthRequest(source);
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 第三方登录回调
     *
     * @param source   登陆类型回调地址
     * @param callback
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/oauth/callback/{source}")
    public Object login(@PathVariable("source") String source, AuthCallback callback, HttpServletResponse response) throws
            IOException {
        if (source == null) return null;
        AuthRequest authRequest = iTripartiteUserService.getAuthRequest(source);
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
        if (authResponse == null) throw new ServiceException("授权失败");
        TripartiteUser tripartiteUser = new TripartiteUser(authResponse);
        tripartiteUser.setUserType(justAuthConfig.getTripartiteUserType());
        iTripartiteUserService.oauthLogin(tripartiteUser);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        //跳转到前端登录中转页，并提供token
        response.sendRedirect(justAuthConfig.getTransferUrl() + "?key=" + authResponse.getData().getUuid()
                + "&token=" + saTokenInfo.getTokenValue());
        return authResponse;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/oauth/getInfo")
    public Map<String, Object> getInfo() {
        Map<String, Object> ajax = new HashMap<>();
        if (!StpUtil.isLogin()) {
            ajax.put("isLogin", false);
            return ajax;
        }
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) {
            ajax.put("isLogin", false);
            return ajax;
        }
        TripartiteUserVo tripartiteUser = iTripartiteUserService.queryById(uuid);
        tripartiteUser.setUuid(uuid);
        ajax.put("user", tripartiteUser);
        Set<String> roles = new HashSet<>();
        ajax.put("roles", roles);
        ajax.put("permissions", roles);
        ajax.put("isLogin", true);
        return ajax;
    }

    /**
     * 前台重置密码
     *
     * @throws IOException
     */
    @RepeatSubmit()
    @PostMapping("/oauth/reset/password")
    public void resetPassword(@Validated @RequestBody RegisterUserMain registerUserMain) {
        iTripartiteUserService.resetPassword(registerUserMain);
    }

    /**
     * 手机号绑定
     *
     * @throws IOException
     */
    @RepeatSubmit()
    @PostMapping("/oauth/phone/binding")
    public void phoneNumberBinding(@Validated @RequestBody PhoneBinding phoneBinding) {
        iTripartiteUserService.phoneNumberBinding(phoneBinding);
    }

    /**
     * 查询登录状态
     *
     * @return
     */
    @RequestMapping("/oauth/isLogin")
    public UserLoginStatusVo isLogin() {
        return iTripartiteUserService.isLogin();
    }


    /**
     * websocket 是否链接
     *
     * @param userid
     * @return
     */
    @GetMapping("/websocket/is-online/{userid}")
    public UserSimpleInfoVo isOnline(@PathVariable(name = "userid") String userid) {
        return iTripartiteUserService.isOnline(userid);
    }

}
