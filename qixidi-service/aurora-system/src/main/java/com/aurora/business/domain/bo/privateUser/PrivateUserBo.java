package com.aurora.business.domain.bo.privateUser;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 私信记录业务对象 private_user
 *
 * @author aurora
 * @date 2023-03-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("私信记录业务对象")
public class PrivateUserBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    /**
     * 目标用户id
     */
    @ApiModelProperty(value = "目标用户id", required = true)
    @NotBlank(message = "目标用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;


}

