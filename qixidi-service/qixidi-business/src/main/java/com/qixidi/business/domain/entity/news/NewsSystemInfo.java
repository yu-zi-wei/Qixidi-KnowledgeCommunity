package com.qixidi.business.domain.entity.news;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;

/**
 * 系统消息对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */
@Data
@TableName("b_news_system_info")
@Accessors(chain = true)
public class NewsSystemInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 消息标题
     */
    @ExcelProperty(value = "消息标题")
    private String newsTitle;
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

    /**
     * 创建时间
     */
    private Date createTime;

}
