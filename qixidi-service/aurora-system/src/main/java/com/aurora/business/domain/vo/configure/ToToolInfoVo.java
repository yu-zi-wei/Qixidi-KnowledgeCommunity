package com.aurora.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 工具信息视图对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@ApiModel("工具信息视图对象")
@ExcelIgnoreUnannotated
public class ToToolInfoVo {

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
    @ExcelProperty(value = "父级id")
    @ApiModelProperty("父级id")
    private Long parentId;

    /**
     * 工具名称
     */
    @ExcelProperty(value = "工具名称")
    @ApiModelProperty("工具名称")
    private String toolName;

    /**
     * 描述
     */
    private String describe;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    @ApiModelProperty("图标")
    private String icon;

    /**
     * 是否为父级（1；父级，2：子级）
     */
    @ExcelProperty(value = "是否为父级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=；父级，2：子级")
    @ApiModelProperty("是否为父级（1；父级，2：子级）")
    private Integer isParent;

    /**
     * 工具地址
     */
    @ExcelProperty(value = "工具地址")
    @ApiModelProperty("工具地址")
    private String toolUrl;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private Long order;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    @ApiModelProperty("类型")
    private Long type;

    /**
     * 状态（0：正常，1：已失效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已失效")
    @ApiModelProperty("状态（0：正常，1：已失效）")
    private Integer state;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    @ApiModelProperty("创建人")
    private String createBy;

    private Date createTime;
}

