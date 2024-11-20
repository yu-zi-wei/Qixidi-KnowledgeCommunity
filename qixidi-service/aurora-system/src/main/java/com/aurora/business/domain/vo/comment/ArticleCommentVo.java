package com.aurora.business.domain.vo.comment;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 文章评论视图对象 article_comment
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@ApiModel("文章评论视图对象")
@ExcelIgnoreUnannotated
public class ArticleCommentVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 文章id
     */
    @ExcelProperty(value = "文章id")
    @ApiModelProperty("文章id")
    private Long articleId;

    /**
     * 文章用户id
     */
    @ExcelProperty(value = "文章用户id")
    @ApiModelProperty("文章用户id")
    private String uid;

    /**
     * 父级评论id
     */
    @ExcelProperty(value = "父级评论id")
    @ApiModelProperty("父级评论id")
    private Long parentId;

    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    @ExcelProperty(value = "评论等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1：一级，2：二级，3：三级及以下")
    @ApiModelProperty("评论等级（1：一级，2：二级，3：三级及以下）")
    private Integer commentGrade;

    /**
     * 目标id
     */
    @ExcelProperty(value = "目标id")
    @ApiModelProperty("目标id")
    private String targetId;

    /**
     * 目标用户id
     */
    @ExcelProperty(value = "目标用户id")
    @ApiModelProperty("目标用户id")
    private String targetUid;

    /**
     * 评论人id
     */
    @ExcelProperty(value = "评论人id")
    @ApiModelProperty("评论人id")
    private String commentUid;

    /**
     * 评论内容
     */
    @ExcelProperty(value = "评论内容")
    @ApiModelProperty("评论内容")
    private String content;

    /**
     * 评论类型（1：文章，2：评论）
     */
    @ExcelProperty(value = "评论类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2：评论")
    @ApiModelProperty("评论类型（1：文章，2：评论）")
    private Integer type;

    /**
     * 评论状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "评论状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    @ApiModelProperty("评论状态（0：正常，1：已删除）")
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

    @ApiModelProperty(value = "目标用户头像")
    private String targetAvatar;

    @ApiModelProperty(value = "目标用户名称")
    private String targetName;

    @ApiModelProperty(value = "评论人用户名称")
    private String commentName;

    @ApiModelProperty(value = "评论人用户头像")
    private String commentAvatar;

    @ApiModelProperty(value = "评论挂载")
    private List mountComment;

    @ApiModelProperty(value = "评论总数")
    private Integer commentTotal;

}

