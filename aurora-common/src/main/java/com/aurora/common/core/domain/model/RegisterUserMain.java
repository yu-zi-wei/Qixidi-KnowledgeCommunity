package com.aurora.common.core.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ApiModel("密码注册对象")
public class RegisterUserMain {

    @NotNull(message = "注册类型检查异常")
    @ApiModelProperty(value = "注册类型（1：邮箱注册）")
    private Integer registerType;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户名
     */
    @NotBlank(message = "邮箱不能空")
    @ApiModelProperty(value = "邮箱")
    private String email;

    //    @NotBlank(message = "验证码不能为空", groups = {AddGroup.class, EditGroup.class})
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;


}
