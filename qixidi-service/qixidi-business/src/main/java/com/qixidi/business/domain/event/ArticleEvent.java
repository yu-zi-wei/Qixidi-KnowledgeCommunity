package com.qixidi.business.domain.event;

import lombok.Getter;

/**
 * 文章事件（创建/更新），事务提交后触发异步处理
 */
@Getter
public class ArticleEvent {

    private final Long articleId;
    private final String articleTitle;
    private final String articleContent;
    private final String articleAbstract;
    private final String authorUuid;
    private final boolean generateAbstract;
    private final boolean needReview;

    public ArticleEvent(Long articleId, String articleTitle, String articleContent,
                        String articleAbstract, String authorUuid,
                        boolean generateAbstract, boolean needReview) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleAbstract = articleAbstract;
        this.authorUuid = authorUuid;
        this.generateAbstract = generateAbstract;
        this.needReview = needReview;
    }
}
