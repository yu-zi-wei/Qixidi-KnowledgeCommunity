package com.qixidi.auth.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 短信登录对象
 *
 * @author Lion Li
 */

@Data
public class SmsLoginBody {

    /**
     * 用户名
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String phonenumber;

    /**
     * 用户密码
     */
    @NotBlank(message = "{sms.code.not.blank}")
    private String smsCode;

}
