package com.qixidi.business.domain.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章特殊Id枚举
 *
 * @author zi-wei
 * @create 2026/4/24 15:47
 */
@Getter
@AllArgsConstructor
public enum ArticleSpecialIdEnums {
    /**
     * - id：（-11：关于栖息地）
     * - id：（-12：友链）
     * - id：（-13：关于作者）
     */
    SITE_INTRODUCTION(-11L, "关于栖息地"),
    FRIENDSHIP_LINK(-12L, "友链"),
    AUTHOR_INTRODUCTION(-13L, "关于作者"),
    ;
    private Long code;
    private String msg;
}
