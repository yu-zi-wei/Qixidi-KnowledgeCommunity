package com.qixidi.business.domain.vo.article;

import lombok.Data;

/**
 * 文章统计计数VO（批量查询用）
 */
@Data
public class ArticleCountVo {
    private Long articleId;
    private int count;
}
