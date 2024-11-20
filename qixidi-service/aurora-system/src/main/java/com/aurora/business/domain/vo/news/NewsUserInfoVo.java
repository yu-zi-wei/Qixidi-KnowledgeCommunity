package com.aurora.business.domain.vo.news;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 用户消息视图对象 news_user_info
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@ApiModel("用户消息视图对象")
@ExcelIgnoreUnannotated
public class NewsUserInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private Long newsId;

    /**
     * 消息目标id
     */
    private Long targetId;

    /**
     * 消息标题
     */
    @ExcelProperty(value = "消息标题")
    @ApiModelProperty("消息标题")
    private String newsTitle;

    @ExcelProperty(value = "消息内容")
    @ApiModelProperty("消息内容")
    private String newsContent;

    private Integer type;

    /**
     * 是否已读（0：未读，1：已读）
     */
    @ExcelProperty(value = "是否已读", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：未读，1：已读")
    @ApiModelProperty("是否已读（0：未读，1：已读）")
    private Integer beenRead;

    /**
     * 发送者id
     */
    @ExcelProperty(value = "发送者id")
    @ApiModelProperty("发送者id")
    private String senderId;

    @ApiModelProperty("发送者名称")
    private String senderName;


    @ApiModelProperty("发送者头像")
    private String senderAvatar;


    /**
     * 接收者id
     */
    @ExcelProperty(value = "接收者id")
    @ApiModelProperty("接收者id")
    private String recipientId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
