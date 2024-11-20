/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:49
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
 * 网站基本信息视图对象 basic_info
 *
 * @author ruoyi
 * @date 2022-11-21
 */
@Data
@ExcelIgnoreUnannotated
public class BasicInfoVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 网站标题
     */
    @ExcelProperty(value = "网站标题")
    private String title;

    /**
     * 介绍
     */
    @ExcelProperty(value = "介绍")
    private String introduce;

    /**
     * 左头像
     */
    @ExcelProperty(value = "左头像")
    private String leftImg;

    private String leftImgId;

    private String rightImgId;

    private String type;

    private Long state;
    /**
     * 恋爱时间前缀
     */
    private String loverPrefix;

    /**
     * 恋爱时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loverTime;
    /**
     * 左名称
     */
    @ExcelProperty(value = "左名称")
    private String leftName;

    /**
     * 右头像
     */
    @ExcelProperty(value = "右头像")
    private String rightImg;

    /**
     * 右名称
     */
    @ExcelProperty(value = "右名称")
    private String rightName;


}
