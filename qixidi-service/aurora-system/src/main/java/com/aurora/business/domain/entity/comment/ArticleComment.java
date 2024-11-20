package com.aurora.business.domain.entity.comment;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文章评论对象 article_comment
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@Accessors(chain = true)
@TableName("article_comment")
public class ArticleComment {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 文章id
     */
    private Long articleId;
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
     * 评论类型（1：文章，2：评论）
     */
    private Integer type;
    /**
     * 评论状态（0：正常，1：已删除）
     */
    @TableField("`state`")
    private Integer state;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
