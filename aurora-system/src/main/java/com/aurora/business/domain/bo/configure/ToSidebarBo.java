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
 * 侧边栏配置业务对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("侧边栏配置业务对象")
public class ToSidebarBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 侧边栏名称
     */
    @ApiModelProperty(value = "侧边栏名称", required = true)
    @NotBlank(message = "侧边栏名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String sidebarName;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", required = true)
    private String sidebarIcon;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    private Long order;
    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址", required = true)
    @NotBlank(message = "路由地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String route;

    /**
     * 是否有下拉（0：没有，1：有）'
     */
    @ApiModelProperty(value = "是否有下拉（0：没有，1：有）'", required = true)
    @NotNull(message = "是否有下拉（0：没有，1：有）'不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isList;

    /**
     * 状态（0：有效1：无效）
     */
    @ApiModelProperty(value = "状态（0：有效1：无效）", required = true)
    private Integer status;


}

