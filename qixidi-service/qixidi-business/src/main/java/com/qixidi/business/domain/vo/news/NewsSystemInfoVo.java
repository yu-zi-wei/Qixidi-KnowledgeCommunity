package com.qixidi.business.domain.vo.news;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 系统消息视图对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class NewsSystemInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 消息标题
     */
    @ExcelProperty(value = "消息标题")
    private String newsTitle;
    /**
     * 状态
     */
    private Long state;
    /**
     * 是否群发（1：群发，2：指定人发）
     */
    private Long isMassAir;
    /**
     * uid
     */
    private String uid;
    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    private String newsContent;

    /**
     * 是否有详情（1：无，2：有）
     */
    @ExcelProperty(value = "是否有详情", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：无，2：有")
    private Long isDetails;

    /**
     * 消息类型（1：系统，2：用户）
     */
    @ExcelProperty(value = "消息类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：系统，2：用户")
    private Long type;

    /**
     * 失效时间
     */
    @ExcelProperty(value = "失效时间")
    private Date lapseTime;


}

