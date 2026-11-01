package com.qixidi.business.domain.vo.news;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户消息记录视图对象 b_news_user_record（发送者/接收者展示字段由 Service 层调用户域接口组装）
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
     * 关联对象id（评论类型指向对应评论表，Service 层回填正文用）
     */
    private Long newsId;

    /**
     * 消息内容（点赞为被赞对象标题；评论正文由 Service 层回填）
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
     * 发送者 uid（b_news_user_record.target_uid，Service 层按它组装展示字段）
     */
    private String senderUid;

    /**
     * 接收者 uid（b_news_user_record.uid，Service 层按它组装展示字段）
     */
    private String recipientUid;

    /**
     * 发送者昵称（Service 层组装）
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 接收者昵称（Service 层组装）
     */
    private String recipientName;

    /**
     * 接收者头像
     */
    private String recipientAvatar;

}
