package com.aurora.web.controller.frontDesk;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.business.service.ITripartiteUserService;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.config.justAuth.JustAuthConfig;
import com.aurora.common.constant.Constants;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginUserMain;
import com.aurora.common.core.domain.model.PhoneBinding;
import com.aurora.common.core.domain.model.RegisterUserMain;
import com.aurora.common.enums.UserStatus;
import com.aurora.common.helper.LoginHelper;
import com.aurora.system.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 【前台】登录验证管理
 *
 * @author ziwei
 * @date 2023年10月15日
 */
@Validated
@RequiredArgsConstructor
@RestController
public class LoginController extends BaseController {
    @Autowired
    private JustAuthConfig justAuthConfig;
    private final SysLoginService loginService;
    private final ITripartiteUserService iTripartiteUserService;


    /**
     * 密码登录
     *
     * @param loginUserMain
     * @return
     */
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

    /**
     * 注册
     *
     * @param registerUserMain
     * @return
     */
    @RepeatSubmit()
    @PostMapping("/oauth/front-desk/register")
    public R register(@Validated @RequestBody RegisterUserMain registerUserMain) {
        registerUserMain.setUserType(justAuthConfig.getTripartiteUserType());
        return iTripartiteUserService.register(registerUserMain);
    }

    /**
     * 注销账号
     *
     * @return
     */
    @RepeatSubmit()
    @GetMapping("/oauth/account/cancellation")
    public R accountCancellation() {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) {
            return R.fail("注销失败！");
        }
        return toAjax(iTripartiteUserService.accountCancellation(uuid) ? 1 : 0);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param mag
     * @return
     */
    @RepeatSubmit()
    @GetMapping("/oauth/front-desk/code/{email}/{mag}")
    public R sendOutCode(@PathVariable("email") String email, @PathVariable("mag") String mag) {
        return iTripartiteUserService.sendOutCode(email, mag);
    }

    /**
     * 发送手机号验证码
     *
     * @param phone
     * @param mag
     * @param request
     * @return
     * @throws Exception
     */
    @RepeatSubmit()
    @GetMapping("/oauth/phone/code/{phone}/{mag}")
    public R sendPhoneCode(@PathVariable("phone") String phone, @PathVariable("mag") String mag, HttpServletRequest request) throws Exception {
        return iTripartiteUserService.sendPhoneCode(phone, mag, request);
    }

    /**
     * 登出
     *
     * @return
     */
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
    @RequestMapping("/oauth/render/{source}")
    public R renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        boolean login = StpUtil.isLogin();
        if (login) {
            return R.fail("请勿重复登录！");
        }
        AuthRequest authRequest = iTripartiteUserService.getAuthRequest(source);
        //生成授权url
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        //将这个url返回给前端Vue,由Vue去执行 授权页
        Map<String, String> map = new HashMap<>();
        map.put("url", authorizeUrl);
        return R.ok(map);
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
        AuthRequest authRequest = iTripartiteUserService.getAuthRequest(source);
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
            .setUserType(justAuthConfig.getTripartiteUserType());
        iTripartiteUserService.oauthLogin(tripartiteUser);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
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
    @RepeatSubmit()
    @PostMapping("/oauth/reset/password")
    public R resetPassword(@Validated @RequestBody RegisterUserMain registerUserMain) {
        return iTripartiteUserService.resetPassword(registerUserMain);
    }

    /**
     * 手机号绑定
     *
     * @throws IOException
     */
    @RepeatSubmit()
    @PostMapping("/oauth/phone/binding")
    public R phoneNumberBinding(@Validated @RequestBody PhoneBinding phoneBinding) {
        return toAjax(iTripartiteUserService.phoneNumberBinding(phoneBinding) ? 1 : 0);
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
