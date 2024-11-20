/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 15:52
 * @version 1.0
 */
package com.aurora.lover.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import com.aurora.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 爱情清单视图对象 repertoire
 *
 * @author ruoyi
 * @date 2022-11-22
 */
@Data
@ExcelIgnoreUnannotated
public class RepertoireVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 清单名称
     */
    @ExcelProperty(value = "清单名称")
    private String name;

    /**
     * 清单内容
     */
    @ExcelProperty(value = "清单内容")
    private String content;

    /**
     * 清单图片
     */
    @ExcelProperty(value = "清单图片")
    private String img;

    /**
     * 状态（0，正常，1，已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=，正常，1，已删除")
    private Long state;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long order;
    private Integer isComplete;


    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;


}
