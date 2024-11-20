package com.aurora.business.domain.bo.collection;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;


/**
 * 收藏夹信息业务对象 collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("收藏夹信息业务对象")
public class CollectionInformationBo extends BaseEntity {

    /**
     * 收藏夹id
     */
    @ApiModelProperty(value = "收藏夹id", required = true)
    private Long id;

    /**
     * 收藏夹名称
     */
    @ApiModelProperty(value = "收藏夹名称", required = true)
    @NotBlank(message = "收藏夹名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String collectionName;

    /**
     * 收藏夹简介
     */
    @ApiModelProperty(value = "收藏夹简介", required = true)
    private String collectionIntroduce;

    /**
     * 收藏夹状态（0：正常，1：未启用）
     */
    @ApiModelProperty(value = "收藏夹状态（0：正常，1：未启用）", required = true)
    private Long state;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", required = true)
    private String username;

    /**
     * 修改者id
     */
    @ApiModelProperty(value = "修改者id", required = true)
    private Long updateId;

    /**
     * 收录数
     */
    private Integer includedCount;
}

