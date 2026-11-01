package com.qixidi.business.domain.vo.news;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户消息记录视图对象 b_news_user_record（后台管理：发送者/接收者名称为 join 产物）
 *
 * @author ziwei
 * @date 2026年11月01日
 */
@Data
@NoArgsConstructor
public class NewsUserRecordVo {

    /**
     * id
     */
    private Long id;

    /**
     * 消息类型（1：评论，2：点赞，3：关注，6：随笔评论，7：小记评论）
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读（0：未读，1：已读）
     */
    private Integer beenRead;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发送者昵称（target_uid join b_user_main）
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 接收者昵称（uid join b_user_main）
     */
    private String recipientName;

    /**
     * 接收者头像
     */
    private String recipientAvatar;

}
