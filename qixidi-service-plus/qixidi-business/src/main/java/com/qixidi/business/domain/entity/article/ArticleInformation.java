package com.qixidi.business.domain.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
    private Long id;
    /**
     * 作者id
     */
    private String userId;
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
     * 文章内容
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
     * 平台类型（1：文章，2：关于作者）
     */
    private Integer platformType;
    /**
     * 转载地址
     */
    private String reprintUrl;
    /**
     * 文章状态（0：正常，1：已删除，2:草稿）
     */
    private Integer state;
    /**
     * 修改者id
     */
    private String updateId;
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
    private String labelId;
    /**
     * 点赞次数
     */
    private Long likeTimes;
    /**
     * 评论次数
     */
    private Long commentTimes;
    /**
     * 收藏次数
     */
    private Long collectionTimes;

    /**
     * 浏览次数
     */
    private Long numberTimes;
    /**
     * 是否公开（1：公开，2：不公开）
     */
    private Integer isPublic;
    /**
     * 审核状态（1：审核中，2：审核通过，3：审核不通过,4:草稿）
     */
    private Integer auditState;
    /**
     * 审核时间
     */
    private Date auditTime;
    private Date updateTime;
    private Date createTime;
    /**
     * 热度权重
     */
    private Double heatWeight;
}
