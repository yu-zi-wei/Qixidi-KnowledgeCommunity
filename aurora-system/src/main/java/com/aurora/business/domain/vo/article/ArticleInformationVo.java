package com.aurora.business.domain.vo.article;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 文章信息视图对象 article_information
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ApiModel("文章信息视图对象")
@ExcelIgnoreUnannotated
public class ArticleInformationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @ExcelProperty(value = "文章id")
    @ApiModelProperty("文章id")
    private Long id;

    /**
     * 作者id
     */
    @ExcelProperty(value = "作者id")
    @ApiModelProperty("作者id")
    private Long userId;

    /**
     * 文章标题
     */
    @ExcelProperty(value = "文章标题")
    @ApiModelProperty("文章标题")
    private String articleTitle;

    /**
     * 文章封面
     */
    @ExcelProperty(value = "文章封面")
    @ApiModelProperty("文章封面")
    private String articleCover;

    /**
     * 文章摘要
     */
    @ExcelProperty(value = "文章摘要")
    @ApiModelProperty("文章摘要")
    private String articleAbstract;

    /**
     * 文章内容
     */
    @ExcelProperty(value = "文章内容")
    @ApiModelProperty("文章内容")
    private String articleContent;

    @ExcelProperty(value = "文章内容md")
    @ApiModelProperty("文章内容md")
    private String articleContentMd;

    @ExcelProperty(value = "主题")
    @ApiModelProperty("主题")
    private String theme;

    /**
     * 文章类型（1：原创，2：转载）
     */
    @ExcelProperty(value = "文章类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：原创，2：转载")
    @ApiModelProperty("文章类型（1：原创，2：转载）")
    private Integer type;

    /**
     * 转载地址
     */
    @ExcelProperty(value = "转载地址")
    @ApiModelProperty("转载地址")
    private String reprintUrl;

    /**
     * 文章状态（0：正常，1：已删除，2：草稿）
     */
    @ExcelProperty(value = "文章状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除，2：草稿")
    @ApiModelProperty("文章状态（0：正常，1：已删除，2：草稿）")
    private Integer state;

    /**
     * 修改者id
     */
    @ExcelProperty(value = "修改者id")
    @ApiModelProperty("修改者id")
    private Long updateId;

    /**
     * 专栏id
     */
    @ExcelProperty(value = "专栏id")
    @ApiModelProperty("专栏id")
    private Long specialId;

    @ExcelProperty(value = "分类d")
    @ApiModelProperty("分类id")
    private Long groupingId;

    /**
     * 标签（多个以逗号隔开）
     */
    @ExcelProperty(value = "标签", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    @ApiModelProperty("标签（多个以逗号隔开）")
    private String babelId;

    /**
     * 点赞次数
     */
    @ExcelProperty(value = "点赞次数")
    @ApiModelProperty("点赞次数")
    private Long likeTimes;
    /**
     * 评论次数
     */
    @ExcelProperty(value = "评论次数")
    @ApiModelProperty("评论次数")
    private Long commentTimes;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = "浏览次数")
    @ApiModelProperty("浏览次数")
    private Long numberTimes;

    /**
     * 是否公开（1：公开，2：不公开）
     */
    @ExcelProperty(value = "是否公开", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：公开，2：不公开")
    @ApiModelProperty("是否公开（1：公开，2：不公开）")
    private Integer isPublic;

    /**
     * 审核状态（1：审核中，2：审核通过，3：审核不通过）
     */
    @ExcelProperty(value = "审核状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：审核中，2：审核通过，3：审核不通过")
    @ApiModelProperty("审核状态（1：审核中，2：审核通过，3：审核不通过）")
    private Integer auditStatus;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private Date auditTime;

    @ExcelProperty(value = "修改时间")
    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ExcelProperty(value = "职业")
    @ApiModelProperty("职业")
    private String occupation;

    private String username;
    private String nickname;
    private String avatar;

    //------------------------
    @ApiModelProperty("点赞数")
    private int fabulousCount;

    @ApiModelProperty("收藏数")
    private int collectionCount;

    @ApiModelProperty("关注数")
    private int followCount;

    @ApiModelProperty("被关注数")
    private int fansCount;

    @ApiModelProperty("评论数")
    private int commentCount;

    @ApiModelProperty("文章数")
    private int articleCount;

    @ApiModelProperty("专栏数")
    private int specialColumnCount;

    @ApiModelProperty("圈子数")
    private int circleCount;
}

