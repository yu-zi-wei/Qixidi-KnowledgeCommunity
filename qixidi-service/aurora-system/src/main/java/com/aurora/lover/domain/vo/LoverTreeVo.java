/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:56
 * @version 1.0
 */
package com.aurora.lover.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * 爱情树视图对象 lover_tree
 *
 * @author ruoyi
 * @date 2022-11-30
 */
@Data
@ExcelIgnoreUnannotated
public class LoverTreeVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 爱情誓言
     */
    @ExcelProperty(value = "爱情誓言")
    private String content;

    /**
     * 恋爱时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "恋爱时间")
    private Date loverTime;

    /**
     * 恋爱时间前缀
     */
    private String loverPrefix;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long state;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;


}
