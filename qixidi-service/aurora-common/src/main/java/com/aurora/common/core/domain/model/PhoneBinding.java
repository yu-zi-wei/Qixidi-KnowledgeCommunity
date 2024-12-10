package com.aurora.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
