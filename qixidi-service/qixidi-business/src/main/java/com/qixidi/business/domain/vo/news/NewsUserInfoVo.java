package com.qixidi.business.domain.vo.news;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.news.NewsUserInfo;


/**
 * 用户消息视图对象 b_news_user_info
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@ExcelIgnoreUnannotated
public class NewsUserInfoVo extends NewsUserInfo {

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
    private String newsTitle;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

}
