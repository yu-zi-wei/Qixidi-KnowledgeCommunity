package com.qixidi.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 侧边栏配置对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@TableName("to_sidebar")
public class ToSidebar {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 父级路由地址
     */
    private Long parentId;
    /**
     * 侧边栏名称
     */
    private String sidebarName;
    /**
     * 图标
     */
    private String sidebarIcon;

    /**
     * 图标颜色
     */
    private String iconColor;

    /**
     * 排序
     */
    @TableField("`order`")
    private Long order;

    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;
    /**
     * 路由地址
     */
    private String route;
    /**
     * 是否有下拉（0：没有，1：有）'
     */
    private Integer isList;
    /**
     * 状态（0：有效1：无效）
     */
    @TableField("`status`")
    private Integer status;

    private Integer type;

}

