package com.aurora.business.domain.bo.topic;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 话题参与信息业务对象 topic_join_info
 *
 * @author aurora
 * @date 2023-03-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("话题参与信息业务对象")
public class TopicJoinInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String uid;

    /**
     * 话题id
     */
    @ApiModelProperty(value = "话题id", required = true)
    @NotNull(message = "话题id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long topicId;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件", required = true)
    @NotBlank(message = "附件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String enclosure;

    /**
     * 附件类型
     */
    @ApiModelProperty(value = "附件类型", required = true)
    @NotBlank(message = "附件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String enclosureType;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已删除）", required = true)
    @NotNull(message = "状态（0：正常，1：已删除）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long state;


}

