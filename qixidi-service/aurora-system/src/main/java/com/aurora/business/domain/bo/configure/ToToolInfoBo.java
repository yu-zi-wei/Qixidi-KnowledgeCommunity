package com.aurora.business.domain.bo.configure;

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
 * 工具信息业务对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("工具信息业务对象")
public class ToToolInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id", required = true)
    private Long parentId;

    /**
     * 工具名称
     */
    @ApiModelProperty(value = "工具名称", required = true)
    @NotBlank(message = "工具名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String toolName;

    /**
     * 描述
     */
    private String describe;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", required = true)
    private String icon;

    /**
     * 是否为父级（1；父级，2：子级）
     */
    @ApiModelProperty(value = "是否为父级（1；父级，2：子级）", required = true)
    @NotNull(message = "是否为父级（1；父级，2：子级）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isParent;

    /**
     * 工具地址
     */
    @ApiModelProperty(value = "工具地址", required = true)
    @NotBlank(message = "工具地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String toolUrl;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long order;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    private Long type;

    /**
     * 状态（0：正常，1：已失效）
     */
    @ApiModelProperty(value = "状态（0：正常，1：已失效）", required = true)
    private Integer state;

    private String createBy;


}

