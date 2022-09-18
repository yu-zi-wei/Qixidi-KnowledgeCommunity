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
 * 导航栏配置业务对象 to_navigation
 *
 * @author aurora
 * @date 2022-09-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("导航栏配置业务对象")
public class ToNavigationBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 导航栏名称
     */
    @ApiModelProperty(value = "导航栏名称", required = true)
    @NotBlank(message = "导航栏名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String navigationName;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", required = true)
    private String navigationIcon;

    /**
     * 路由地址
     */
    @ApiModelProperty(value = "路由地址", required = true)
    @NotBlank(message = "路由地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String route;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long order;

    /**
     * 是否有下拉（0：没有，1：有）
     */
    @ApiModelProperty(value = "是否有下拉（0：没有，1：有）", required = true)
    @NotNull(message = "是否有下拉（0：没有，1：有）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isList;

    /**
     * 状态（0：有效，1：失效）
     */
    @ApiModelProperty(value = "状态（0：有效，1：失效）", required = true)
    @NotNull(message = "状态（0：有效，1：失效）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;


}
