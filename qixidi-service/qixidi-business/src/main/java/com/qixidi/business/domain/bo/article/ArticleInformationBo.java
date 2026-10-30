package com.qixidi.business.domain.bo.article;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 文章信息业务对象 b_article_information
 *
 * @author aurora
 * @date 2022-08-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleInformationBo extends BaseEntity {
    /**
     * 文章id
     */
    private Long id;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 作者名称
     */
    private String nickname;

    /**
     * 文章标题
     */
    @NotBlank(message = "请填写文章标题", groups = {AddGroup.class, EditGroup.class})
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
    @NotBlank(message = "请填写文章内容", groups = {AddGroup.class, EditGroup.class})
    private String articleContent;

    /**
     * 主题
     */
    private String theme;


    /**
     * 文章类型（1：原创，2：转载，3：翻译）
     */
    @NotNull(message = "请选择文章类型", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 转载地址
     */
    private String reprintUrl;

    /**
     * 文章状态（0：正常，1：已删除）
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

    @NotNull(message = "请选择文章分类", groups = {AddGroup.class, EditGroup.class})
    private Long groupingId;

    /**
     * 标签（多个以逗号隔开）
     */
    @NotBlank(message = "请填写标签", groups = {AddGroup.class, EditGroup.class})
    private String labelId;

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
     * 审核状态多选（getArticleInfo in 查询；与 auditState 同时传时为 AND 关系）
     */
    private List<Integer> auditStates;

    /**
     * 文章类型多选（后台列表筛选，in 查询）
     */
    private List<Integer> types;

    /**
     * 分类 id 多选（后台列表筛选，in 查询）
     */
    private List<Long> groupingIds;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 是否选择ai生成摘要
     */
    private Boolean abstractSelect;

}

