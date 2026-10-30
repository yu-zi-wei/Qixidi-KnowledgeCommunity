package com.qixidi.business.domain.vo.dictum;

import lombok.Data;

import java.util.Date;
import java.util.List;
import com.qixidi.business.domain.entity.dictum.DictumComment;

/**
 * 文章评论对象 b_dictum_comment
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
public class DictumCommentVo extends DictumComment {

    /**
     * 次级评论集合
     */
    List<DictumCommentVo> dictumCommentVoList;
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
