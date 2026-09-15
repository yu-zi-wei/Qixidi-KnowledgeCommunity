package com.qixidi.business.domain.vo.timeNotes;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 时光小记评论视图对象 b_time_notes_comment
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Data
public class TimeNotesCommentVo {

    /**
     * 次级评论集合
     */
    private List<TimeNotesCommentVo> children;
    /**
     * id
     */
    private Long id;
    /**
     * 时光小记id
     */
    private Long timeNotesId;
    /**
     * 小记用户id
     */
    private String uid;
    /**
     * 父级评论id（一级评论 = 小记id）
     */
    private Long parentId;
    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    private Integer commentGrade;
    /**
     * 目标id（被回复的评论id或小记id）
     */
    private Long targetId;
    /**
     * 目标用户id
     */
    private String targetUid;
    /**
     * 评论人id
     */
    private String commentUid;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论类型（1：小记，2：评论）
     */
    private Integer type;
    /**
     * 评论状态（0：正常，1：已删除）
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 评论用户名
     */
    private String username;
    /**
     * 评论用户昵称
     */
    private String nickname;
    /**
     * 评论用户头像
     */
    private String avatar;
    /**
     * 目标评论用户名
     */
    private String targetUsername;
    /**
     * 目标评论用户昵称
     */
    private String targetNickname;
    /**
     * 目标评论用户头像
     */
    private String targetAvatar;
}
