package com.qixidi.business.domain.bo.shield;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 屏蔽词库业务对象 to_shield_word
 *
 * @author aurora
 * @date 2023-02-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToShieldWordBo extends BaseEntity {

    /**
     *id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 屏蔽字
     */
    @NotBlank(message = "屏蔽字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String keyword;

    /**
     * 类型（1：文章，2：评论）
     */
    @NotNull(message = "类型（1：文章，2：评论）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 屏蔽词权重
     */
    @NotNull(message = "屏蔽词权重不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long weight;

    /**
     * 状态（0：正常，1：已失效）
     */
    @NotNull(message = "状态（0：正常，1：已失效）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long state;


}
