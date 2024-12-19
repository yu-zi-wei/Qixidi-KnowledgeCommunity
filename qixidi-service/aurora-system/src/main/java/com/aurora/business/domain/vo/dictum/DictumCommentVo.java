package com.aurora.business.domain.vo.dictum;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章评论对象 b_dictum_comment
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
public class DictumCommentVo {

    /**
     * 次级评论集合
     */
    List<DictumCommentVo> dictumCommentVoList;
    /**
     * id
     */
    private Long id;
    /**
     * 名言id
     */
    private Long dictumId;
    /**
     * 文章用户id
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
     * 评论内容
     */
    private String content;
    /**
     * 评论类型（1：名言，2：评论）
     */
    private Integer type;
    /**
     * 评论状态（0：正常，1：已删除）
     */
    private Integer status;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
