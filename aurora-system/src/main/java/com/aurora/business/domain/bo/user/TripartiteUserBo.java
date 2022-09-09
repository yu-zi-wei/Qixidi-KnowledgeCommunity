package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 【请填写功能名称】业务对象 tripartite_user
 *
 * @author ruoyi
 * @date 2022-06-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("【请填写功能名称】业务对象")
public class TripartiteUserBo extends BaseEntity {

    /**
     * 用户第三方系统的唯一id
     */
    @ApiModelProperty(value = "用户第三方系统的唯一id", required = true)
    @NotBlank(message = "用户第三方系统的唯一id不能为空", groups = {EditGroup.class})
    private String uuid;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String username;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nickname;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String password;

    /**
     * 盐
     */
    @ApiModelProperty(value = "盐", required = true)
    @NotBlank(message = "盐不能为空", groups = {AddGroup.class, EditGroup.class})
    private String salt;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像", required = true)
    @NotBlank(message = "用户头像不能为空", groups = {AddGroup.class, EditGroup.class})
    private String avatar;

    /**
     * 用户网址
     */
    @ApiModelProperty(value = "用户网址", required = true)
    @NotBlank(message = "用户网址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String blog;

    /**
     * 所在公司
     */
    @ApiModelProperty(value = "所在公司", required = true)
    @NotBlank(message = "所在公司不能为空", groups = {AddGroup.class, EditGroup.class})
    private String company;

    /**
     * 位置
     */
    @ApiModelProperty(value = "位置", required = true)
    @NotBlank(message = "位置不能为空", groups = {AddGroup.class, EditGroup.class})
    private String location;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱", required = true)
    @NotBlank(message = "用户邮箱不能为空", groups = {AddGroup.class, EditGroup.class})
    private String email;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", required = true)
    @NotBlank(message = "性别不能为空", groups = {AddGroup.class, EditGroup.class})
    private String gender;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    @ApiModelProperty(value = "用户备注（各平台中的用户个人介绍）", required = true)
    @NotBlank(message = "用户备注（各平台中的用户个人介绍）不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;

    /**
     * 用户来源
     */
    @ApiModelProperty(value = "用户来源", required = true)
    @NotBlank(message = "用户来源不能为空", groups = {AddGroup.class, EditGroup.class})
    private String source;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型", required = true)
    @NotBlank(message = "用户类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String userType;

    /**
     * 用户状态（0：正常，1：冻结，2：已删除）
     */
    @ApiModelProperty(value = "用户状态（0：正常，1：冻结，2：已删除）", required = true)
    @NotNull(message = "用户状态（0：正常，1：冻结，2：已删除）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer state;

    /**
     * 最后登录ip
     */
    @ApiModelProperty(value = "最后登录ip", required = true)
    @NotBlank(message = "最后登录ip不能为空", groups = {AddGroup.class, EditGroup.class})
    private String loginIp;

    /**
     * 角色（1，普通用户，2，vip用户）
     */
    @ApiModelProperty(value = "角色（1，普通用户，2，vip用户）", required = true)
    @NotNull(message = "角色（1，普通用户，2，vip用户）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long roleId;
}
