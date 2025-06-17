package com.qixidi.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnums {

    /**
     * 密码登录
     */
    PASSWORD("密码错误次数过多，账户锁定 %s 分钟", "密码输入错误 %s 次"),

    /**
     * 短信登录
     */
    SMS("短信验证码错误次数过多，账户锁定 %s 分钟", "短信验证码输入错误 %s 次"),

    /**
     * 小程序登录
     */
    XCX("", "");

    /**
     * 登录重试超出限制提示
     */
    final String retryLimitExceed;

    /**
     * 登录重试限制计数提示
     */
    final String retryLimitCount;
}
