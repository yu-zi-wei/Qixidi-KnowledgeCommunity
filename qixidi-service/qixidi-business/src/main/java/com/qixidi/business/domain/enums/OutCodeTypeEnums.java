package com.qixidi.business.domain.enums;

import lombok.Getter;

/**
 * 验证码发送类型
 *
 * @author zi-wei
 * @create 2025/4/7 9:57
 */
@Getter
public enum OutCodeTypeEnums {
    SIGN_IN(1, "注册"),
    RESET_PASSWORD(2, "重置密码"),
    MAILBOX_BINDING(3, "邮箱绑定"),
    PHONE_BINDING(4, "手机号绑定"),
    ;
    private Integer code;
    private String message;

    OutCodeTypeEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String acquireTypeMessage(Integer code) {
        for (OutCodeTypeEnums value : values()) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return null;
    }
}
