package com.aurora.business.domain.bo.collection;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 文章收藏关联业务对象 collection_record
 *
 * @author aurora
 * @date 2022-11-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章收藏关联业务对象")
public class CollectionRecordBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", required = true)
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标标签id
     */
    private String labelId;

    /**
     * 收藏夹id
     */
    @ApiModelProperty(value = "收藏夹id", required = true)
    @NotNull(message = "收藏夹id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long collectionId;
    /**
     * 转移前收藏夹id
     */
    @ApiModelProperty(value = "收藏夹id", required = true)
    private Long shiftCollectionId;

    /**
     * 收藏类型
     */
    @ApiModelProperty(value = "收藏类型", required = true)
    private Integer type;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已删除）", required = true)
    private Integer state;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id", required = true)
    private String uid;

}

