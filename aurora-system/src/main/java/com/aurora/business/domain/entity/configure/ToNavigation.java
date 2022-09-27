package com.aurora.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 导航栏配置对象 to_navigation
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@TableName("to_navigation")
public class ToNavigation {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 导航栏名称
     */
    private String navigationName;
    /**
     * 图标
     */
    private String navigationIcon;
    /**
     * 路由地址
     */
    private String route;
    /**
     * 排序
     */
    @TableField("`order`")
    private Long order;
    /**
     * 是否有下拉（0：没有，1：有）
     */
    private Integer isList;
    /**
     * 状态（0：有效，1：失效）
     */
    @TableField("`status`")
    private Integer status;

}
