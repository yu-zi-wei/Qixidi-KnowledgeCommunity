/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:19
 * @version 1.0
 */
package com.aurora.lover.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 关于我们视图对象 about
 *
 * @author ruoyi
 * @date 2022-11-30
 */
@Data
@ExcelIgnoreUnannotated
public class AboutVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    private String title;
    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long state;


}
