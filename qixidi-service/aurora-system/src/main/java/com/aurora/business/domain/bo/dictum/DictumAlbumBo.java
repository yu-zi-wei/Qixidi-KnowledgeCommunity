package com.aurora.business.domain.bo.dictum;

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
 * 名言专辑业务对象 b_dictum_album
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("名言专辑业务对象")
public class DictumAlbumBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * uid
     */
    @ApiModelProperty(value = "uid", required = true)
    private String uid;

    /**
     * 收录总数
     */
    private Long employSum;

    /**
     * 专辑名称
     */
    @ApiModelProperty(value = "专辑名称", required = true)
    @NotBlank(message = "专辑名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面", required = true)
    private String cover;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介", required = true)
    private String briefIntroduction;

    /**
     * 专辑状态（1：公开，2：私有，3：关注可看）
     */
    @ApiModelProperty(value = "专辑状态（1：公开，2：私有，3：关注可看）", required = true)
    @NotNull(message = "专辑状态（1：公开，2：私有，3：关注可看）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long albumState;

    /**
     * 点赞总数
     */
    @ApiModelProperty(value = "点赞总数", required = true)
    private Long helpSum;

    /**
     * 关注总数
     */
    @ApiModelProperty(value = "关注总数", required = true)
    private Long followSum;

    /**
     * 状态（0：正常吗：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常吗：已删除）", required = true)
    private Integer state;


}

