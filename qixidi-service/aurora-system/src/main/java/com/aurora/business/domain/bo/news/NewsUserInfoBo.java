package com.aurora.business.domain.bo.news;

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
 * 用户消息业务对象 news_user_info
 *
 * @author aurora
 * @date 2022-11-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户消息业务对象")
public class NewsUserInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsContent;

    /**
     * 消息类型（1：点赞消息，2：评论消息，3：关注消息）
     */
    @ApiModelProperty(value = "消息类型 NewsType枚举", required = true)
    @NotNull(message = "消息类型 不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    @ApiModelProperty("生产消息的目标者id")
    private Long prnNewsWorksId;

    @NotNull(message = "消息目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    @ApiModelProperty("消息目标id")
    private Long worksId;

    @ApiModelProperty("目标消息父级id")
    private Long worksParentId;

    @ApiModelProperty("目标消息父级Uid")
    private String worksParentUid;

    @NotNull(message = "目标消息类型不能为空", groups = {AddGroup.class, EditGroup.class})
    @ApiModelProperty("消息目标类型（1：文章，2：评论，3：圈子，4：活动）")
    private Integer worksType;

    @ApiModelProperty("消息目标内容")
    private String worksContent;
    /**
     * 状态（0：正常，1：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已删除）", required = true)
    private Integer state;

    /**
     * 是否已读（0：未读，1：已读）
     */
    @ApiModelProperty(value = "是否已读（0：未读，1：已读）", required = true)
    private Integer beenRead;

    /**
     * 发送者id
     */
    @ApiModelProperty(value = "发送者id", required = true)
    @NotNull(message = "发送者id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String senderId;

    /**
     * 接收者id
     */
    @ApiModelProperty(value = "接收者id", required = true)
    @NotNull(message = "接收者id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String recipientId;


}
