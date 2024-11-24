package com.aurora.business.domain.bo.fabulous;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;


/**
 * 点赞业务对象 b_fabulous_record
 *
 * @author aurora
 * @date 2022-10-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("点赞业务对象")
public class FabulousRecordBo extends BaseEntity {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 对应的作品id
     */
    @ApiModelProperty(value = "对应的作品id", required = true)
    @NotNull(message = "对应的作品id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long typeId;
    /**
     * 点赞对象的标签
     */
    private String labelId;
    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", required = true)
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    @NotBlank(message = "目标标题", groups = {AddGroup.class, EditGroup.class})
    @ApiModelProperty(value = "目标标题", required = true)
    private String targetTitle;

    @ApiModelProperty(value = "目标Uid", required = true)
    @NotBlank(message = "目标Uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 点赞类型（1：文章，2：评论）
     */
    @ApiModelProperty(value = "点赞类型（1：文章，2：评论）", required = true)
    @NotNull(message = "点赞类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 状态（0：点赞有效，1：取消点赞）
     */
    @ApiModelProperty(value = "状态（0：点赞有效，1：取消点赞）", required = true)
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long state;

    @ApiModelProperty(value = "文章点赞总数", required = true)
    private Integer fabulousSum;

}
