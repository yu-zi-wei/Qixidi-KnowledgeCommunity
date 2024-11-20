package com.aurora.business.domain.vo.news;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ziwei
 * @date 2024年02月26日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentNewsVo {

    /**
     * 评论id
     */
    private Long id;
    /**
     * 消息id
     */
    private Long newsId;
    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

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
     * 评论人名称
     */
    private String commentName;

    @ApiModelProperty("发送者头像")
    private String commentAvatar;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论类型（1：文章，2：评论）
     */
    private Integer type;

    /**
     * 是否已读
     */
    private int beenRead;
    private Date createTime;
}
