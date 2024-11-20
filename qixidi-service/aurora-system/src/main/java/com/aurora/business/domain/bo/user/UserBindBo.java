package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户换绑bo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBindBo extends BaseEntity {

    private String uuid;

    /**
     * 操作类型（1：绑定邮箱，2：换绑邮箱，3：换绑手机号）
     */
    @NotNull(message = "操作类型异常", groups = {AddGroup.class, EditGroup.class})
    private Integer type;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 原数据（如果为首次绑定及为当前数据）
     */
    private String originalData;
}
