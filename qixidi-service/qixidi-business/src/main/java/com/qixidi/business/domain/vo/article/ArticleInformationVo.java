package com.qixidi.business.domain.vo.article;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.excel.convert.ExcelDictConvert;
import com.qixidi.business.domain.entity.label.LabelInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;
import com.qixidi.business.domain.entity.article.ArticleInformation;



/**
 * 文章信息视图对象 b_article_information
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class ArticleInformationVo extends ArticleInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String groupingName;

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
     * 是否点赞（当前用户）
     */
    private Boolean isFabulous;

    /**
     * 是否收藏
     */
    private Boolean isCollection;

    /**
     * 收藏夹id
     */
    @ExcelProperty(value = "收藏夹id")
    private Long collectionId;

    /**
     * 收藏记录id
     */
    private Long collectionRecordId;

    /**
     * 评论次数
     */
    @ExcelProperty(value = "评论次数")
    private Long commentTimes = 0L;

    /**
     * 收藏次数
     */
    @ExcelProperty(value = "收藏次数")
    private Integer collectionTimes = 0;

    /**
     * 用户信息
     */
    @ExcelProperty(value = "职业")
    private String occupation;

    /**
     * 用户角色
     */
    private Integer roleId;

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

    // 作者统计字段（用于文章详情页展示作者信息）
    private int fabulousCount;
    private int fansFabulousCount;
    private int collectionCount;
    private int followCount;
    private int fansFollowCount;
    private int commentCount;
    private int fansCommentCount;
    private int articleCount;
    private int specialColumnCount;
    private int fansSpecialColumn;
    private int circleCount;
}

