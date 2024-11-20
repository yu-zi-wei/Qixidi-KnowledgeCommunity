/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:12
 * @version 1.0
 */
package com.aurora.lover.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 爱情轮播图视图对象 lover_carousel
 *
 * @author ruoyi
 * @date 2022-11-29
 */
@Data
@ExcelIgnoreUnannotated
public class LoverCarouselVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 图片
     */
    @ExcelProperty(value = "图片")
    private String img;

    /**
     * 图片id
     */
    @ExcelProperty(value = "图片id")
    private String imgId;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String introduce;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long state;


}
