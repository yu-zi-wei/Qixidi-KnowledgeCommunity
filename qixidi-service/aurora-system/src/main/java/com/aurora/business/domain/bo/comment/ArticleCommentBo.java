package com.aurora.business.domain.bo.comment;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文章评论业务对象 b_article_comment
 *
 * @author aurora
 * @date 2022-11-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章评论业务对象")
public class ArticleCommentBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id", required = true)
    @NotNull(message = "文章id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long articleId;

    @ApiModelProperty(value = "目标内容", required = true)
    private String worksContent;

    /**
     * 文章用户id
     */
    @ApiModelProperty(value = "文章用户id", required = true)
    @NotBlank(message = "文章用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    /**
     * 父级评论id
     */
    @ApiModelProperty(value = "父级评论id", required = true)
    @NotNull(message = "父级评论id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long parentId;

    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    @ApiModelProperty(value = "评论等级（1：一级，2：二级，3：三级及以下）", required = true)
    @NotNull(message = "评论等级不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer commentGrade;

    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", required = true)
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标用户id
     */
    @ApiModelProperty(value = "目标用户id", required = true)
    @NotBlank(message = "目标用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 评论人id
     */
    @ApiModelProperty(value = "评论人id", required = true)
    private String commentUid;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 评论类型（1：文章，2：评论）
     */
    @ApiModelProperty(value = "评论类型（1：文章，2：评论）", required = true)
    @NotNull(message = "评论类型（1：文章，2：评论）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 评论状态（0：正常，1：已删除）
     */
    @ApiModelProperty(value = "评论状态（0：正常，1：已删除）", required = true)
    private Integer state;

}
