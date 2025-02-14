package com.qixidi.business.domain.enums;

/**
 * 评论 点赞 类型
 */
public enum CommentType {
    ARTICLE_TYPE(1, "文章"),
    COMMENT_TYPE(2, "评论"),
    CIRCLE_NEWS_TYPE(3, "圈子"),
    ACTIVITY_NEWS_TYPE(4, "活动");

    private Integer code;
    private String value;

    CommentType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
