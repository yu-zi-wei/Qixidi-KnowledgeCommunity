package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserHomeBo extends BaseEntity {

    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    @NotNull(message = "type不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    @NotBlank(message = "uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    private String orderType;

}
