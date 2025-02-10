package com.qixidi.business.domain.bo.collection;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;


/**
 * 文章收藏关联业务对象 b_collection_record
 *
 * @author aurora
 * @date 2022-11-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionRecordBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 目标id
     */
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标标签id
     */
    private String labelId;

    /**
     * 收藏夹id
     */
    @NotNull(message = "收藏夹id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long collectionId;
    /**
     * 转移前收藏夹id
     */
    private Long shiftCollectionId;

    /**
     * 收藏类型
     */
    private Integer type;

    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;

    /**
     * 创建者id
     */
    private String uid;

}

