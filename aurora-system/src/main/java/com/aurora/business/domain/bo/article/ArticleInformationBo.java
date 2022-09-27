package com.aurora.business.domain.bo.article;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 文章信息业务对象 article_information
 *
 * @author aurora
 * @date 2022-08-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章信息业务对象")
public class ArticleInformationBo extends BaseEntity {
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id", required = true)
    private Long id;

    /**
     * 作者id
     */
    @ApiModelProperty(value = "作者id", required = true)
    private Long userId;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", required = true)
    @NotBlank(message = "请填写文章标题", groups = {AddGroup.class, EditGroup.class})
    private String articleTitle;

    /**
     * 文章封面
     */
    @ApiModelProperty(value = "文章封面", required = true)
    private String articleCover;

    /**
     * 文章摘要
     */
    @ApiModelProperty(value = "文章摘要", required = true)
    @NotBlank(message = "请填写文章摘要", groups = {AddGroup.class, EditGroup.class})
    private String articleAbstract;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容html", required = true)
    @NotBlank(message = "请填写文章内容", groups = {AddGroup.class, EditGroup.class})
    private String articleContent;

    /**
     * 文章内容md
     */
    @ApiModelProperty(value = "文章内容md", required = true)
    @NotBlank(message = "请填写文章内容", groups = {AddGroup.class, EditGroup.class})
    private String articleContentMd;

    @ApiModelProperty(value = "主题", required = true)
    private String theme;


    /**
     * 文章类型（1：原创，2：转载）
     */
    @ApiModelProperty(value = "文章类型（1：原创，2：转载）", required = true)
    @NotNull(message = "请选择文章类型", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 转载地址
     */
    @ApiModelProperty(value = "转载地址", required = true)
    private String reprintUrl;

    /**
     * 文章状态（0：正常，1：已删除，2：草稿）
     */
    @ApiModelProperty(value = "文章状态（0：正常，1：已删除，2：草稿）", required = true)
    private Integer state;

    /**
     * 修改者id
     */
    @ApiModelProperty(value = "修改者id", required = true)
    private Long updateId;

    /**
     * 专栏id
     */
    @ApiModelProperty(value = "专栏id", required = true)
//    @NotNull(message = "请选择发布专栏", groups = {AddGroup.class, EditGroup.class})
    private Long specialId;

    @ApiModelProperty(value = "分类id", required = true)
    @NotNull(message = "请选择文章分类", groups = {AddGroup.class, EditGroup.class})
    private Long groupingId;

    /**
     * 标签（多个以逗号隔开）
     */
    @ApiModelProperty(value = "标签（多个以逗号隔开）", required = true)
    @NotBlank(message = "请填写标签", groups = {AddGroup.class, EditGroup.class})
    private String babelId;

    /**
     * 点赞次数
     */
    @ApiModelProperty(value = "点赞次数", required = true)
    private Long likeTimes;

    /**
     * 浏览次数
     */
    @ApiModelProperty(value = "浏览次数", required = true)
    private Long numberTimes;

    /**
     * 是否公开（1：公开，2：不公开）
     */
    @ApiModelProperty(value = "是否公开（1：公开，2：不公开）", required = true)
    private Integer isPublic;

    /**
     * 审核状态（1：审核中，2：审核通过，3：审核不通过）
     */
    @ApiModelProperty(value = "审核状态（1：审核中，2：审核通过，3：审核不通过）", required = true)
    private Integer auditStatus;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间", required = true)
    private Date auditTime;


}

