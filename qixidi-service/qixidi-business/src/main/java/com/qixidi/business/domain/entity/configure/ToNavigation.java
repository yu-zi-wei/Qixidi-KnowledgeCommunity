package com.qixidi.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


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
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 父级路由地址
     */
    private Long parentId;
    /**
     * 导航栏名称
     */
    @ExcelProperty(value = "导航栏名称")
    private String navigationName;
    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String navigationIcon;
    /**
     * 图标颜色
     */
    private String iconColor;
    /**
     * 路由地址
     */
    @ExcelProperty(value = "路由地址")
    private String route;
    /**
     * 排序
     */
    @TableField("`order`")
    @ExcelProperty(value = "排序")
    private Long order;
    /**
     * 是否有下拉（0：没有，1：有）
     */
    @ExcelDictFormat(readConverterExp = "0=：没有，1：有")
    private Integer isList;
    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;
    /**
     * 状态（0：有效，1：失效）
     */
    @TableField("`status`")
    @ExcelDictFormat(readConverterExp = "0=：有效，1：失效")
    private Integer status;

    @ExcelDictFormat(readConverterExp = "1=：主页路由2：用户页面路由")
    private Integer type;
}
