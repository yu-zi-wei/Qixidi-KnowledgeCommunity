package com.aurora.business.domain.vo.configure;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 侧边栏配置视图对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@ApiModel("侧边栏配置视图对象")
@ExcelIgnoreUnannotated
public class ToSidebarVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 侧边栏名称
     */
    @ExcelProperty(value = "侧边栏名称")
    @ApiModelProperty("侧边栏名称")
    private String sidebarName;

    /**
     * 图标颜色
     */
    private String iconColor;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    @ApiModelProperty("图标")
    private String sidebarIcon;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private Long order;

    /**
     * 限（0：公开，1：普通用户，2：vip用户）
     */
    private Integer jurisdiction;


    /**
     * 路由地址
     */
    @ExcelProperty(value = "路由地址")
    @ApiModelProperty("路由地址")
    private String route;
    /**
     * 是否有下拉（0：没有，1：有）'
     */
    @ExcelProperty(value = "是否有下拉", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：没有，1：有")
    @ApiModelProperty("是否有下拉（0：没有，1：有）'")
    private Integer isList;

    /**
     * 状态（0：有效1：无效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：有效1：无效")
    @ApiModelProperty("状态（0：有效1：无效）")
    private Integer status;

    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：主页路由2：用户页面路由")
    @ApiModelProperty("状态（1：主页路由2：用户页面路由）")
    private Integer type;

    /**
     * 二级菜单列表
     */
    private List<ToSidebarVo> levelList;

}
