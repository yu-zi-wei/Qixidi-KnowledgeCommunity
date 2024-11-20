package com.aurora.business.domain.vo.shield;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 屏蔽词库视图对象 to_shield_word
 *
 * @author aurora
 * @date 2023-02-20
 */
@Data
@ApiModel("屏蔽池库视图对象")
@ExcelIgnoreUnannotated
public class ToShieldWordVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private Long id;

    /**
     * 屏蔽字
     */
    @ExcelProperty(value = "屏蔽字")
    @ApiModelProperty("屏蔽字")
    private String keyword;

    /**
     * 类型（1：文章，2：评论）
     */
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2：评论")
    @ApiModelProperty("类型（1：文章，2：评论）")
    private Long type;

    /**
     * 屏蔽词权重
     */
    @ExcelProperty(value = "屏蔽词权重")
    @ApiModelProperty("屏蔽词权重")
    private Long weight;

    /**
     * 状态（0：正常，1：已失效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已失效")
    @ApiModelProperty("状态（0：正常，1：已失效）")
    private Long state;


}
