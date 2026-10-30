package com.qixidi.business.domain.vo.timeNotes;

import lombok.Data;

import java.util.Date;
import java.util.List;
import com.qixidi.business.domain.entity.timeNotes.TimeNotesComment;

/**
 * 时光小记评论视图对象 b_time_notes_comment
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Data
public class TimeNotesCommentVo extends TimeNotesComment {

    /**
     * 次级评论集合
     */
    private List<TimeNotesCommentVo> children;
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
