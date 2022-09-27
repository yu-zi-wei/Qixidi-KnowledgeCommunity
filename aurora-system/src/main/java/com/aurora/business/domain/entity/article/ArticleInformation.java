package com.aurora.business.domain.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 文章信息对象 article_information
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@TableName("article_information")
public class ArticleInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 作者id
     */
    private Long userId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章封面
     */
    private String articleCover;
    /**
     * 文章摘要
     */
    private String articleAbstract;
    /**
     * 文章内容
     */
    private String articleContent;
    /**
     * 文章内容md
     */
    private String articleContentMd;
    /**
     * 主题
     */
    private String theme;
    /**
     * 文章类型（1：原创，2：转载）
     */
    private Integer type;
    /**
     * 转载地址
     */
    private String reprintUrl;
    /**
     * 文章状态（0：正常，1：已删除，2：草稿）
     */
    private Integer state;
    /**
     * 修改者id
     */
    private Long updateId;
    /**
     * 专栏id
     */
    private Long specialId;
    /**
     * 分类id
     */
    private Long groupingId;

    /**
     * 标签（多个以逗号隔开）
     */
    private String babelId;
    /**
     * 点赞次数
     */
    private Long likeTimes;
    /**
     * 浏览次数
     */
    private Long numberTimes;
    /**
     * 是否公开（1：公开，2：不公开）
     */
    private Integer isPublic;
    /**
     * 审核状态（1：审核中，2：审核通过，3：审核不通过）
     */
    private Integer auditStatus;
    /**
     * 审核时间
     */
    private Date auditTime;
    private Date updateTime;
    private Date createTime;

}
