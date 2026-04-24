package com.qixidi.business.service.count;

import cn.hutool.core.collection.CollUtil;
import com.qixidi.business.domain.vo.article.ArticleCountVo;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文章统计计数查询助手
 * 提供按文章ID批量查询评论数、收藏数、点赞数
 */
@Component
@RequiredArgsConstructor
public class ArticleCountQueryHelper {

    private final ArticleCommentMapper commentMapper;
    private final CollectionRecordMapper collectionMapper;
    private final FabulousRecordMapper fabulousMapper;

    /**
     * 批量查评论数
     */
    public Map<Long, Integer> commentCount(Collection<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) return Collections.emptyMap();
        List<ArticleCountVo> list = commentMapper.selectCommentCountByArticleIds(articleIds);
        return toMap(list);
    }

    /**
     * 批量查收藏数（仅文章类型收藏）
     */
    public Map<Long, Integer> collectionCount(Collection<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) return Collections.emptyMap();
        List<ArticleCountVo> list = collectionMapper.selectCollectionCountByArticleIds(articleIds);
        return toMap(list);
    }

    /**
     * 批量查点赞数（从 DB 查 b_fabulous_record）
     */
    public Map<Long, Integer> likeCount(Collection<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) return Collections.emptyMap();
        List<ArticleCountVo> list = fabulousMapper.selectLikeCountByArticleIds(articleIds);
        return toMap(list);
    }

    /**
     * 查单篇文章的点赞数
     */
    public int likeCount(Long articleId) {
        Map<Long, Integer> map = likeCount(List.of(articleId));
        return map.getOrDefault(articleId, 0);
    }

    private Map<Long, Integer> toMap(List<ArticleCountVo> list) {
        if (CollUtil.isEmpty(list)) return Collections.emptyMap();
        return list.stream().collect(Collectors.toMap(ArticleCountVo::getArticleId, ArticleCountVo::getCount));
    }
}
