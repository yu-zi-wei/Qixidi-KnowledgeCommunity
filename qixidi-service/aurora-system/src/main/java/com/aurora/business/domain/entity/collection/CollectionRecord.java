package com.aurora.business.domain.entity.collection;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 文章收藏关联对象 b_collection_record
 *
 * @author aurora
 * @date 2022-11-10
 */
@Data
@TableName("b_collection_record")
public class CollectionRecord {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 目标id
     */
    private Long targetId;
    /**
     * 收藏夹id
     */
    private Long collectionId;
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
    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
