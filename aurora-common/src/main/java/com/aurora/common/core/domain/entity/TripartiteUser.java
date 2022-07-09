package com.aurora.common.core.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.helper.LoginHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@TableName("user_main")
@ApiModel("第三方登录用户")
@Accessors(chain = true)
public class TripartiteUser {
    private static final long serialVersionUID = 1L;

    /**
     * 用户第三方系统的唯一id
     */
    @ApiModelProperty(value = "用户第三方系统的唯一id", required = true)
    @NotBlank(message = "用户第三方系统的唯一id不能为空", groups = {EditGroup.class})
    @TableId(value = "uuid")
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
    private String nickname;
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
    @ApiModelProperty(value = "用户来源", required = true)
    @NotBlank(message = "用户来源不能为空", groups = {AddGroup.class, EditGroup.class})
    private String source;
    /**
     * 用户类型
     */
    private String userType;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        return userType + LoginHelper.JOIN_CODE + uuid;
    }

}
