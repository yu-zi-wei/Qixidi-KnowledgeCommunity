package com.aurora.business.domain.bo.shield;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 屏蔽词库业务对象 to_shield_word
 *
 * @author aurora
 * @date 2023-02-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("屏蔽池库业务对象")
public class ToShieldWordBo extends BaseEntity {

    /**
     *
     */
    @ApiModelProperty(value = "", required = true)
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 屏蔽字
     */
    @ApiModelProperty(value = "屏蔽字", required = true)
    @NotBlank(message = "屏蔽字不能为空", groups = { AddGroup.class, EditGroup.class })
    private String keyword;

    /**
     * 类型（1：文章，2：评论）
     */
    @ApiModelProperty(value = "类型（1：文章，2：评论）", required = true)
    @NotNull(message = "类型（1：文章，2：评论）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 屏蔽词权重
     */
    @ApiModelProperty(value = "屏蔽词权重", required = true)
    @NotNull(message = "屏蔽词权重不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long weight;

    /**
     * 状态（0：正常，1：已失效）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已失效）", required = true)
    @NotNull(message = "状态（0：正常，1：已失效）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long state;


}
