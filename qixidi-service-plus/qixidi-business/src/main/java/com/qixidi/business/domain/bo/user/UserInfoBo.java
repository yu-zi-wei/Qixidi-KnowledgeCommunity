package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoBo extends BaseEntity {

    /**
     * 用户id
     */
    @NotBlank(message = "用户第三方系统的唯一id不能为空", groups = {EditGroup.class})
    private String uuid;

    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    private Integer type;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nickname;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户网址
     */
    private String blog;

    /**
     * 所在公司
     */
    private String company;

    /**
     * 位置
     */
    private String location;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 个人主页地址
     */
    private String homepage;
    /**
     * 个人简介
     */
    private String introduce;
    /**
     * 更新时间
     */
    private Date updateTime;
}
