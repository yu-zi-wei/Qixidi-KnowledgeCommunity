package com.qixidi.business.domain.vo.news;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 用户消息视图对象（b_news_user_record：点赞/关注列表；b_news_system_info：系统消息列表）
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@NoArgsConstructor
public class NewsUserInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 消息id
     */
    private Long newsId;

    /**
     * 消息内容
     */
    private String newsContent;

    /**
     * 消息标题（系统消息）
     */
    private String newsTitle;

    /**
     * 消息类型（1：评论消息，2：点赞消息，3：关注消息，4系统消息）
     */
    private Integer type;

    /**
     * 消息目标id
     */
    private Long targetId;

    /**
     * 是否已读（0：未读，1：已读）
     */
    private Integer beenRead;

    /**
     * 发送者id
     */
    private String senderId;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 接收者id
     */
    private String recipientId;

    /**
     * 创建时间
     */
    private Date createTime;

}
