package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 平台用户业务对象 tripartite_user
 *
 * @author ziwei
 * @date 2022-06-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TripartiteUserBo extends BaseEntity {

    /**
     * 用户第三方系统的唯一id
     */
    private String uuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nickname;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 用户头像
     */
    private String avatar;

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
     * 职业
     */
    private String occupation;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    private String remark;

    /**
     * 用户来源
     */
    @NotBlank(message = "用户来源不能为空", groups = {AddGroup.class, EditGroup.class})
    private String source;

    /**
     * 用户类型
     */
    @NotBlank(message = "用户类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String userType;

    /**
     * 用户状态（0：正常，1：冻结，2：已删除）
     */
    private Integer state;

    /**
     * 最后登录ip
     */
    private String loginIp;

    /**
     * 角色（1，普通用户，2，vip用户）
     */
    private Long roleId;
}
