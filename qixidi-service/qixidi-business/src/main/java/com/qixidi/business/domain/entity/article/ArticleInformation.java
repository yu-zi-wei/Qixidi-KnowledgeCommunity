package com.qixidi.business.domain.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;

/**
 * 文章信息对象 b_article_information
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@Accessors(chain = true)
@TableName("b_article_information")
public class ArticleInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "文章id")
    private Long id;
    /**
     * 作者id
     */
    @ExcelProperty(value = "作者id")
    private String userId;
    /**
     * 文章标题
     */
    @ExcelProperty(value = "文章标题")
    private String articleTitle;
    /**
     * 文章封面
     */
    @ExcelProperty(value = "文章封面")
    private String articleCover;
    /**
     * 文章摘要
     */
    @ExcelProperty(value = "文章摘要")
    private String articleAbstract;
    /**
     * 文章内容
     */
    @ExcelProperty(value = "文章内容")
    private String articleContent;
    /**
     * 主题
     */
    @ExcelProperty(value = "主题")
    private String theme;
    /**
     * 文章类型（1：原创，2：转载）
     */
    @ExcelDictFormat(readConverterExp = "1=：原创，2：转载")
    private Integer type;
    /**
     * 平台类型（1：文章，2：关于作者）
     */
    private Integer platformType;
    /**
     * 转载地址
     */
    @ExcelProperty(value = "转载地址")
    private String reprintUrl;
    /**
     * 文章状态（0：正常，1：已删除）
     */
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    private Integer state;
    /**
     * 修改者id
     */
    @ExcelProperty(value = "修改者id")
    private String updateId;
    /**
     * 专栏id
     */
    @ExcelProperty(value = "专栏id")
    private Long specialId;
    /**
     * 分类id
     */
    @ExcelProperty(value = "分类d")
    private Long groupingId;

    /**
     * 标签（多个以逗号隔开）
     */
    @ExcelProperty(value = "标签", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    private String labelId;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = "浏览次数")
    private Long numberTimes;
    /**
     * 是否公开（1：公开，2：不公开）
     */
    @ExcelDictFormat(readConverterExp = "1=：公开，2：不公开")
    private Integer isPublic;
    /**
     * 审核状态（1：审核中，2：审核通过，3：审核不通过,4:草稿）
     */
    @ExcelDictFormat(readConverterExp = "1=：审核中，2：审核通过，3：审核不通过，4:草稿")
    private Integer auditState;
    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    private Date auditTime;

    @ExcelProperty(value = "修改时间")
    private Date updateTime;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 热度权重
     */
    private Double heatWeight;
}
