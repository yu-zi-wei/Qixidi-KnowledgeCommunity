package com.aurora.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@ApiModel("手机号绑定（解绑）对象")
public class PhoneBinding {
    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @NotBlank(message = "请输入验证码")
    @ApiModelProperty(value = "验证码")
    private String code;
}
