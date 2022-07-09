package com.aurora.business.domain.vo.label;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 标签信息视图对象 label_info
 *
 * @author ruoyi
 * @date 2022-07-09
 */
@Data
@ApiModel("标签信息视图对象")
@ExcelIgnoreUnannotated
public class LabelInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 分组id
     */
    @ExcelProperty(value = "分组id")
    @ApiModelProperty("分组id")
    private Long labelGroupingId;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    @ApiModelProperty("名称")
    private String labelName;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    @ApiModelProperty("描述")
    private String labelDescribe;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    @ApiModelProperty("封面")
    private String labelCover;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    @ApiModelProperty("状态（0：正常，1：已删除）")
    private Integer state;


}

