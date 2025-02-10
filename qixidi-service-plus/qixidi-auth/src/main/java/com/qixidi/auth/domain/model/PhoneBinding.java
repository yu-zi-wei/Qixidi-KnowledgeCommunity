package com.qixidi.auth.domain.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneBinding {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 验证码
     */
    @NotBlank(message = "请输入验证码")
    private String code;
}
