package com.aurora.business.domain.vo.article;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.business.domain.entity.count.CountUserWebsiteEntity;
import com.aurora.business.domain.entity.label.LabelInfo;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 文章信息视图对象 b_article_information
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class ArticleInformationVo extends CountUserWebsiteEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
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
     * 收藏夹id
     */
    @ExcelProperty(value = "收藏夹id")
    private Long collectionId;
    /**
     * 收藏夹文章id
     */
    private Long collectionRecordId;

    /**
     * 分类id
     */
    @ExcelProperty(value = "分类d")
    private Long groupingId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String groupingName;

    /**
     * 标签（多个以逗号隔开）
     */
    @ExcelProperty(value = "标签", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    private String labelId;
    /**
     * 标签列表
     */
    private List<LabelInfo> labelList;
    /**
     * 标签名称列表
     */
    private List<String> labelNameList;

    /**
     * 标签id列表
     */
    private List<Long> labelLongList;

    /**
     * 点赞次数
     */
    @ExcelProperty(value = "点赞次数")
    private Long likeTimes = 0L;
    /**
     * 是否点赞
     */
    private Set<String> fabulousUserSet;
    /**
     * 是否收藏
     */
    private Boolean isCollection;
    /**
     * 收藏夹文章id
     */
    private Long collectionRId;

    /**
     * 评论次数
     */
    @ExcelProperty(value = "评论次数")
    private Long commentTimes = 0L;
    /**
     * 收藏次数
     */
    @ExcelProperty(value = "收藏次数")
    private Long collectionTimes = 0L;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = "浏览次数")
    private Long numberTimes = 0L;

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
    private Long heatWeight = 0L;

    /**
     * 用户信息
     */
    @ExcelProperty(value = "职业")
    private String occupation;
    /**
     * 账号名称
     */
    private String username;
    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 是否被关注
     */
    private Boolean isFollow = false;

    /**
     * 关注按钮状态
     */
    private Boolean buttonLoading = false;
}

