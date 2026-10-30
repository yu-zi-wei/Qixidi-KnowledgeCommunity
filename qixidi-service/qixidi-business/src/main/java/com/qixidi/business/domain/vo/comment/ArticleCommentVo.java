package com.qixidi.business.domain.vo.comment;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.qixidi.business.domain.entity.comment.ArticleComment;


/**
 * 文章评论视图对象 b_article_comment
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@ExcelIgnoreUnannotated
public class ArticleCommentVo extends ArticleComment {

    private static final long serialVersionUID = 1L;

    /**
     * 目标用户头像
     */
    private String targetAvatar;

    /**
     * 目标用户名称
     */
    private String targetName;

    /**
     * 评论人用户名称
     */
    private String commentName;
    /**
     * 评论人用户头像
     */
    private String commentAvatar;
    /**
     * 评论挂载
     */
    private List<ArticleCommentVo> mountComment = new ArrayList<>();
    /**
     * 评论总数
     */
    private Integer commentTotal;

}

