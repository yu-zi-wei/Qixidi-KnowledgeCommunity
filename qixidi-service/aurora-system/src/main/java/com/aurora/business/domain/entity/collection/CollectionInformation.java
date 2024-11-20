package com.aurora.business.domain.entity.collection;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


/**
 * 收藏夹信息对象 collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */
@Data
@TableName("collection_information")
public class CollectionInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏夹id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 收藏夹名称
     */
    private String collectionName;
    /**
     * 收藏夹简介
     */
    private String collectionIntroduce;
    /**
     * 收藏夹状态（0：正常，1：未启用）
     */
    private Long state;
    /**
     * 用户id
     */
    private String uid;

    private Date createTime;
    /**
     * 修改者id
     */
    private String updateId;

    private Date updateTime;

    /**
     * 收录数
     */
    private Integer includedCount;
}

