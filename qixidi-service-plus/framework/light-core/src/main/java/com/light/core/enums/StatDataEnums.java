package com.light.core.enums;

public enum StatDataEnums {
    USER_COUNT("userCount", "用户总数"),
    ONLINE_USER_COUNT("onlineUserCount", "在线用户总数"),
    ARTICLE_COUNT("articleCount", "文章总数"),
    SPECIAL_COUNT("specialCount", "专栏总数"),
    FAVORITES_COUNT("favoritesCount", "收藏夹总数"),
    DICTUM_GROUP_COUNT("dictumGroupCount", "名言分类总数"),
    DICTUM_ALBUM_COUNT("dictumAlbumCount", "名言专辑总数"),
    DICTUM_COUNT("dictumCount", "名言总数"),
    ;
    private String code;
    private String value;

    StatDataEnums(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
