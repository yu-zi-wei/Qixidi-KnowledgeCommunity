package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户信息对象")
public class UserInfoBo extends BaseEntity {

    @NotBlank(message = "用户第三方系统的唯一id不能为空", groups = {EditGroup.class})
    private String uuid;

    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    private Integer type;

    @ApiModelProperty(value = "用户昵称", required = true)
    @NotBlank(message = "用户昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nickname;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    /**
     * 用户网址
     */
    @ApiModelProperty(value = "用户网址", required = true)
    private String blog;

    /**
     * 所在公司
     */
    @ApiModelProperty(value = "所在公司", required = true)
    private String company;

    /**
     * 位置
     */
    @ApiModelProperty(value = "位置", required = true)
    private String location;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱", required = true)
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 职业
     */
    @ApiModelProperty(value = "职业", required = true)
    private String occupation;

    /**
     * 个人主页地址
     */
    @ApiModelProperty(value = "个人主页地址", required = true)
    private String homepage;
    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简介", required = true)
    private String introduce;

    private Date updateTime;
}
