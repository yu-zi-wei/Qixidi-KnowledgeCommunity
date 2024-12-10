package com.aurora.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterUserMain {
    /**
     * 操作类型（1、手机号注册：2、密码重置）
     */
    @NotNull(message = "操作类型检查异常")
    private Integer registerType;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户名
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 用户密码
     */
    @NotBlank(message = "请设置密码")
    private String password;
    /**
     * 验证码
     */
    @NotBlank(message = "请输入验证码")
    private String code;

}
