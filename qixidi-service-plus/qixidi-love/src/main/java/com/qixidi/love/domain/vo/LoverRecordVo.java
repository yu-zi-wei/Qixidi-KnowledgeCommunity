/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:50
 * @version 1.0
 */
package com.qixidi.love.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;
import com.light.excel.convert.ExcelDictConvert;
import com.light.core.core.domain.BaseEntity;
import lombok.Data;


/**
 * 爱情记录视图对象 lover_record
 *
 * @author ziwei
 * @date 2022-11-21
 */
@Data
@ExcelIgnoreUnannotated
public class LoverRecordVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;


    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    private Long state;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;


}
