package com.qixidi.auth.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    private String phone;

    /**
     * 邮箱
     */
    private String email;

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
