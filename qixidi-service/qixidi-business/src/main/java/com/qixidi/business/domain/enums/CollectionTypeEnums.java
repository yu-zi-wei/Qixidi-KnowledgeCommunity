package com.qixidi.business.domain.enums;

/**
 * 收藏类型
 */
public enum CollectionTypeEnums {
    ARTICLE_TYPE(1, "文章"),
    COMMENT_TYPE(2, "评论");

    private Integer code;
    private String value;

    CollectionTypeEnums(Integer code, String value) {
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
