package com.aurora.business.domain.vo.label;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 标签分组信息视图对象 b_label_grouping_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class LabelGroupingInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    private String groupingName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 文章数
     */
    private Long articleNumber;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    private Integer state;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String groupingDescribe;

    private Long createBy;

    private Date createTime;

}
