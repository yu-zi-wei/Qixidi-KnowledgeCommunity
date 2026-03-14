package com.qixidi.business.domain.bo.privateUser;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 私信记录业务对象 b_private_user
 *
 * @author aurora
 * @date 2023-03-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateUserBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    /**
     * 目标用户id
     */
    @NotBlank(message = "目标用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;


}

