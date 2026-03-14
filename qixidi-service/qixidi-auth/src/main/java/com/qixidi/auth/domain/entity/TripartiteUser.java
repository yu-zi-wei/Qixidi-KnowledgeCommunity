package com.qixidi.auth.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qixidi.auth.helper.LoginHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("b_user_main")
@Accessors(chain = true)
public class TripartiteUser {
    /**
     * 用户第三方系统的唯一id
     */
    @TableId(value = "uuid")
    private String uuid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * A 币数量
     */
    private Long aCurrency;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
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
    private String source;
    /**
     * 用户类型
     */
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
     * 角色（1，普通用户，2，创作者，3，管理员）
     *
     * @see com.qixidi.auth.domain.enums.UserRoleEnums
     */
    private Long roleId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 职业
     */
    private String occupation;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        return userType + LoginHelper.JOIN_CODE + uuid;
    }

    public TripartiteUser(AuthResponse<AuthUser> authResponse) {
        this.uuid = authResponse.getData().getUuid();
        this.username = authResponse.getData().getUsername();
        this.nickname = authResponse.getData().getNickname();
        this.avatar = authResponse.getData().getAvatar();
        this.blog = authResponse.getData().getBlog();
        this.company = authResponse.getData().getCompany();
        this.location = authResponse.getData().getLocation();
        this.email = authResponse.getData().getEmail();
        this.gender = authResponse.getData().getGender().getCode();
        this.remark = authResponse.getData().getRemark();
        this.source = authResponse.getData().getSource();
    }
}
