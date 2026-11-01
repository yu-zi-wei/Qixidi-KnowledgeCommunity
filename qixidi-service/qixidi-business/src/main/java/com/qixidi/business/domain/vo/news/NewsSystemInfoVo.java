package com.qixidi.business.domain.vo.news;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.qixidi.business.domain.entity.news.NewsSystemInfo;


/**
 * 系统消息视图对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
@EqualsAndHashCode(callSuper = true)
public class NewsSystemInfoVo extends NewsSystemInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 接收人昵称（uid join b_user_main，群发行为 null）
     */
    private String recipientName;

    /**
     * 接收人头像（uid join b_user_main，群发行为 null）
     */
    private String recipientAvatar;

}
