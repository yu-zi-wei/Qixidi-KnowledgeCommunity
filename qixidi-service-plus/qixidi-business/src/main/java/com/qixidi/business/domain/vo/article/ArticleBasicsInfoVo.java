package com.qixidi.business.domain.vo.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import lombok.Data;

import java.util.Date;

/**
 * @author zi-wei
 * @create 2025/6/16 17:58
 */
@Data
public class ArticleBasicsInfoVo {
    /**
     * 文章id
     */
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
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    public ArticleBasicsInfoVo(ArticleInformation item) {
        this.id = item.getId();
        this.userId = item.getUserId();
        this.articleTitle = item.getArticleTitle();
        this.createTime = item.getCreateTime();
    }
}
