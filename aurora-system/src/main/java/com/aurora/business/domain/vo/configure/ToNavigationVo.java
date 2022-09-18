package com.aurora.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 导航栏配置视图对象 to_navigation
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@ApiModel("导航栏配置视图对象")
@ExcelIgnoreUnannotated
public class ToNavigationVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 导航栏名称
     */
    @ExcelProperty(value = "导航栏名称")
    @ApiModelProperty("导航栏名称")
    private String navigationName;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    @ApiModelProperty("图标")
    private String navigationIcon;

    /**
     * 路由地址
     */
    @ExcelProperty(value = "路由地址")
    @ApiModelProperty("路由地址")
    private String route;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private Long order;

    /**
     * 是否有下拉（0：没有，1：有）
     */
    @ExcelProperty(value = "是否有下拉", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：没有，1：有")
    @ApiModelProperty("是否有下拉（0：没有，1：有）")
    private Integer isList;

    /**
     * 状态（0：有效，1：失效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：有效，1：失效")
    @ApiModelProperty("状态（0：有效，1：失效）")
    private Integer status;


}
