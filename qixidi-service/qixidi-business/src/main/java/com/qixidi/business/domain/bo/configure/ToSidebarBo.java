package com.qixidi.business.domain.bo.configure;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 侧边栏配置业务对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToSidebarBo extends BaseEntity {

    /**
     * id
     */
    private Long id;
    /**
     * 父级路由地址
     */
    private Long parentId;
    /**
     * 侧边栏名称
     */
    @NotBlank(message = "侧边栏名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String sidebarName;

    /**
     * 图标
     */
    private String sidebarIcon;

    /**
     * 排序
     */
    private Long order;

    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;
    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String route;

    /**
     * 是否有下拉（0：没有，1：有）'
     */
    @NotNull(message = "是否有下拉（0：没有，1：有）'不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isList;

    /**
     * 状态（0：有效1：无效）
     */
    private Integer status;

    /**
     * 类型
     */
    private Integer type;


}

