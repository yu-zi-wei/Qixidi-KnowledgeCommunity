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
 * 私信用户业务对象 private_news_info
 *
 * @author aurora
 * @date 2023-03-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("私信用户业务对象")
public class PrivateNewsInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsComment;

    /**
     * 目标uid
     */
    @ApiModelProperty(value = "目标uid", required = true)
    @NotBlank(message = "目标uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String replyTargetUid;

    /**
     * 是否已读（1：未读，2：已读）
     */
    private Integer beenRead;

}

