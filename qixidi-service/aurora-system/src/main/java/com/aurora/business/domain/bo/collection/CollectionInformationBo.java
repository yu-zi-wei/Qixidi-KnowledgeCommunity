package com.aurora.business.domain.bo.collection;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 收藏夹信息业务对象 b_collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionInformationBo extends BaseEntity {

    /**
     * 收藏夹id
     */
    private Long id;

    /**
     * 收藏夹名称
     */
    @NotBlank(message = "收藏夹名称不能为空", groups = {AddGroup.class, EditGroup.class})
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

    /**
     * 用户名称
     */
    private String username;

    /**
     * 修改者id
     */
    private Long updateId;

    /**
     * 收录数
     */
    private Integer includedCount;
}

