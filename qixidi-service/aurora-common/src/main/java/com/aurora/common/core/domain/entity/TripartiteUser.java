package com.aurora.common.core.domain.entity;


import com.aurora.common.helper.LoginHelper;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("user_main")
@ApiModel("第三方登录用户")
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
     * 角色（1，普通用户，2，vip用户）
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

}
