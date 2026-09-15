package com.qixidi.business.domain.vo.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 时光小记评论消息 VO（/news 评论消息 - 小记 tab）
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeNotesCommentNewsVo {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 消息id
     */
    private Long newsId;
    /**
     * 时光小记id
     */
    private Long timeNotesId;

    /**
     * 时光小记标题
     */
    private String title;

    /**
     * 小记用户id
     */
    private String uid;
    /**
     * 父级评论id
     */
    private Long parentId;
    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    private Integer commentGrade;
    /**
     * 目标id
     */
    private String targetId;
    /**
     * 目标用户id
     */
    private String targetUid;

    /**
     * 评论人id
     */
    private String commentUid;

    /**
     * 评论人名称
     */
    private String commentName;
    /**
     * 评论人头像
     */
    private String commentAvatar;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论类型（1：小记，2：评论）
     */
    private Integer type;

    /**
     * 是否已读
     */
    private int beenRead;

    /**
     * 创建时间
     */
    private Date createTime;
}
