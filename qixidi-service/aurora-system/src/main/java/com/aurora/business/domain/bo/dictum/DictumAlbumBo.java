package com.aurora.business.domain.bo.dictum;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
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
public class DictumAlbumBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * uid
     */
    private String uid;

    /**
     * 收录总数
     */
    private Long employSum;

    /**
     * 专辑名称
     */
    @NotBlank(message = "专辑名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String briefIntroduction;

    /**
     * 专辑状态（1：公开，2：私有，3：关注可看）
     */
    @NotNull(message = "专辑状态（1：公开，2：私有，3：关注可看）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long albumState;

    /**
     * 点赞总数
     */
    private Long helpSum;

    /**
     * 关注总数
     */
    private Long followSum;

    /**
     * 状态（0：正常吗：已删除）
     */
    private Integer state;


}

