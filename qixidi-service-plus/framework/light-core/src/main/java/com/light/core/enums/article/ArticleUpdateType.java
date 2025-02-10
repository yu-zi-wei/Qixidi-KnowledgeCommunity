package com.light.core.enums.article;

public enum ArticleUpdateType {
    FABULOUS_COUNT(1, "点赞数"),
    COMMENT_COUNT(2, "评论数 "),
    COLLECTION_COUNT(3, "收藏数"),
    BROWSE_COUNT(4, "浏览次数");

    private Integer code;
    private String value;

    ArticleUpdateType(Integer code, String value) {
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
