package com.qixidi.business.domain.bo.configure;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 导航栏配置业务对象 to_navigation
 *
 * @author aurora
 * @date 2022-09-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToNavigationBo extends BaseEntity {

    /**
     * id
     */
    private Long id;
    /**
     * 父级路由地址
     */
    private Long parentId;
    /**
     * 导航栏名称
     */
    @NotBlank(message = "导航栏名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String navigationName;

    /**
     * 图标
     */
    private String navigationIcon;

    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String route;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long order;

    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;

    /**
     * 是否有下拉（0：没有，1：有）
     */
    @NotNull(message = "是否有下拉（0：没有，1：有）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isList;

    /**
     * 状态（0：有效，1：失效）
     */
    @NotNull(message = "状态（0：有效，1：失效）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;
    /**
     * 类型
     */
    private Integer type;
}
