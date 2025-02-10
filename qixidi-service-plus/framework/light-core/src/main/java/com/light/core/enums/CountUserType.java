package com.light.core.enums;

public enum CountUserType {

    FABULOUS_COUNT(1, "点赞数"),
    FANS_FABULOUS_COUNT(2, "被点赞数"),
    COLLECTION_COUNT(3, "收藏夹数"),
    FOLLOW_COUNT(4, "关注数 "),
    FANS_FOLLOW_COUNT(5, "被关注数 "),
    COMMENT_COUNT(6, "评论数 "),
    FANS_COMMENT_COUNT(7, "被评论数 "),
    ARTICLE_COUNT(8, "文章数 "),
    SPECIAL_COLUMN_COUNT(9, "专栏数 "),
    FANS_SPECIAL_COLUMN(10, "被关注专栏数 "),
    CIRCLE_COUNT(11, "圈子数 "),
    ALBUM_COUNT(12, "专辑数"),
    b_dictum_info_COUNT(13, "名言数"),
    ;
    private Integer code;
    private String value;

    CountUserType(Integer code, String value) {
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
