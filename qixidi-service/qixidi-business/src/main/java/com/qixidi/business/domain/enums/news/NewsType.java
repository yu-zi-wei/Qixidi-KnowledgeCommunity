package com.qixidi.business.domain.enums.news;

/**
 * 消息类型
 * 20 以上 为自定义类型 (独立的表数据)
 * 评论子类型（随笔 6、小记 7）不单独展示，未读数合并进"评论"（type=1）
 */
public enum NewsType {
    COMMENT_NEWS(1, "评论", "/news/comment"),
    FABULOUS_NEWS(2, "点赞", "/news/fabulous"),
    FOLLOW_NEWS(3, "关注", "/news/follow"),
    PRIVATE_LETTER(4, "私信", "/news/private-letter"),
    SYSTEM_NEWS(5, "系统消息", "/news/system"),
    DICTUM_COMMENT_NEWS(6, "随笔评论", "/news/comment"),
    TIME_NOTES_COMMENT_NEWS(7, "小记评论", "/news/comment"),

    ;
    private Integer code;
    private String value;
    private String route;

    NewsType(Integer code, String value, String route) {
        this.code = code;
        this.value = value;
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public static String getValues(Integer code) {
        for (NewsType value : NewsType.values()) {
            if (value.code.equals(code)) {
                return value.getValue();
            }
        }
        return null;
    }

    public static String getRoutes(Integer code) {
        for (NewsType value : NewsType.values()) {
            if (value.code.equals(code)) {
                return value.getRoute();
            }
        }
        return null;
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
