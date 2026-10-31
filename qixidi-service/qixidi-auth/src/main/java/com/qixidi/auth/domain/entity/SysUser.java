package com.qixidi.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.light.core.annotation.Sensitive;
import com.light.core.constant.UserConstants;
import com.light.core.core.domain.BaseEntity;
import com.light.core.enums.SensitiveStrategy;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author Lion Li
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 用户ID
     *
     * 表 user_id 无 AUTO_INCREMENT（历史数据为雪花 ID），必须显式 ASSIGN_ID：
     * 裸 @TableId 会落全局 id-type=AUTO，insert 不带 ID 列直接报 doesn't have a default value
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 用户账号
     */
//    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String userName;

    /**
     * 用户昵称
     */
//    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    private String nickName;

    /**
     * 用户类型（sys_user系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     *
     * WRITE_ONLY：序列化不输出（日志/响应不泄密）、反序列化可注入（新增/重置密码接口收 body）。
     * 原写法 getter 上 @JsonIgnore + @JsonProperty 是 Jackson 2 的 split 语义；
     * Jackson 3 下 @JsonIgnore 同 accessor 全赢，password 反序列化被忽略 →
     * user.getPassword()=null → hashpw(null) 把 "null" 字面量存库，账号建出来登录必失败
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(
            insertStrategy = FieldStrategy.NOT_EMPTY,
            updateStrategy = FieldStrategy.NOT_EMPTY,
            whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 帐号状态多选筛选（in 查询，后台列表页）
     */
    @TableField(exist = false)
    private List<String> statuses;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色对象
     */
    @TableField(exist = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false)
    private Long[] roleIds;

    public SysUser(Long userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.userId);
    }

}
