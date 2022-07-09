package com.aurora.business.domain.bo.label;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 标签信息业务对象 label_info
 *
 * @author ruoyi
 * @date 2022-07-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("标签信息业务对象")
public class LabelInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id", required = true)
    @NotNull(message = "分组id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long labelGroupingId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String labelName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String labelDescribe;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面", required = true)
    @NotBlank(message = "封面不能为空", groups = { AddGroup.class, EditGroup.class })
    private String labelCover;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已删除）", required = true)
    @NotNull(message = "状态（0：正常，1：已删除）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer state;


}

