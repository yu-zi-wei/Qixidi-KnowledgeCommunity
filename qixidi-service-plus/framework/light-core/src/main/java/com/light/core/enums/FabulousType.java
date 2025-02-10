package com.light.core.enums;

/**
 * 点赞类型
 */
public enum FabulousType {
    ARTICLE_TYPE(1, "文章"),
    ACTIVITY_TYPE(2, "活动"),
    CIRCLE_NEWS_TYPE(3, "圈子"),
    COMMENT_TYPE(4, "评论"),
    ;
    private Integer code;
    private String value;

    FabulousType(Integer code, String value) {
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
