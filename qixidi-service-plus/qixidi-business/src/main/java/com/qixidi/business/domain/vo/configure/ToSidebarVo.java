package com.qixidi.business.domain.vo.configure;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;
import lombok.Data;

import java.util.List;


/**
 * 侧边栏配置视图对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@ExcelIgnoreUnannotated
public class ToSidebarVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 侧边栏名称
     */
    @ExcelProperty(value = "侧边栏名称")
    private String sidebarName;

    /**
     * 图标颜色
     */
    private String iconColor;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String sidebarIcon;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long order;

    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;


    /**
     * 路由地址
     */
    @ExcelProperty(value = "路由地址")
    private String route;
    /**
     * 是否有下拉（0：没有，1：有）'
     */
    @ExcelDictFormat(readConverterExp = "0=：没有，1：有")
    private Integer isList;

    /**
     * 状态（0：有效1：无效）
     */
    @ExcelDictFormat(readConverterExp = "0=：有效1：无效")
    private Integer status;

    @ExcelDictFormat(readConverterExp = "1=：主页路由2：用户页面路由")
    private Integer type;

    /**
     * 二级菜单列表
     */
    private List<ToSidebarVo> levelList;

}
