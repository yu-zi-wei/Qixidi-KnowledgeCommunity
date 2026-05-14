package com.qixidi.business.execute;

import com.qixidi.business.domain.event.ArticleEvent;
import com.qixidi.business.service.article.IArticleInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 文章事件监听器，事务提交后异步执行
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleEventListener {

    private final IArticleInformationService articleInformationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onArticleEvent(ArticleEvent event) {
        log.info("文章事件触发，articleId: {}", event.getArticleId());
        try {
            articleInformationService.handleArticleAsync(event);
        } catch (Exception e) {
            log.error("文章异步处理失败，articleId: {}", event.getArticleId(), e);
        }
    }
}
