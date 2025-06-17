package com.qixidi.system.service;

import cn.dev33.satoken.secure.BCrypt;
import com.light.core.constant.Constants;
import com.light.core.constant.UserConstants;
import com.light.core.core.service.LogininforService;
import com.light.core.utils.ServletUtils;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.light.redission.utils.RedisUtils;
import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.auth.domain.enums.UserTypeEnums;
import com.qixidi.auth.domain.model.RegisterBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 注册校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysRegisterService {

    private final ISysUserService userService;
    private final ISysConfigService configService;
    private final LogininforService asyncService;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        HttpServletRequest request = ServletUtils.getRequest();
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserTypeEnums.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
//        if (captchaOnOff) {
//            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid(), request);
//        }

        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            throw new ServiceException(String.format("保存用户 %s 失败，注册账号已存在", username));
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new ServiceException("注册失败，请联系系统管理员");
        }
        asyncService.recordLogininfor(username, Constants.REGISTER, "注册成功", request);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            asyncService.recordLogininfor(username, Constants.REGISTER, "验证码失效", request);
            throw new ServiceException("验证码失效");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            asyncService.recordLogininfor(username, Constants.REGISTER, "验证码错误", request);
            throw new ServiceException("验证码错误");
        }
    }
}
