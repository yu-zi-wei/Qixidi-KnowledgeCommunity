package com.qixidi.business.domain.enums;

public enum RedisBusinessKeyEnums {
    /**
     * 点赞
     */
    TOTAL_LIKE_COUNT_KEY("TOTAL_LIKE_COUNT_KEY", "文章总点赞总数"),
    USER_LIKE_ARTICLE_KEY("USER_LIKE_ARTICLE_KEY", "用户点赞的文章列表"),
    ARTICLE_LIKED_USER_KEY("ARTICLE_LIKED_USER_KEY", "点赞文章的用户列表"),
    /**
     * 文章浏览
     */
    ARTICLE_GLANCE_OVER("ARTICLE_GLANCE_OVER:%s:%s", "文章浏览记录：文章id：ip"),
    ARTICLE_INTIMACY("ARTICLE_INTIMACY:GROUP:%s", "文章分组亲密度:用户id"),

    /**
     * 验证码
     */
    MAIL_CAPTCHA("MAIL_CAPTCHA:%s", "邮箱验证码:邮箱"),
    PHONE_CAPTCHA("PHONE_CAPTCHA:%s", "手机验证码:手机号"),
    CAPTCHA_DAILY_LIMIT_PHONE("CAPTCHA_DAILY_LIMIT_PHONE:%s", "验证码每日限制:手机号"),
    CAPTCHA_DAILY_LIMIT_IP("CAPTCHA_DAILY_LIMIT_IP:%s", "验证码每日限制:IP地址"),

    /**
     * 导航栏配置
     */
    NAVIGATION_BAR_CONFIGURATION("NAVIGATION_BAR_CONFIGURATION", "导航栏配置"),
    SIDEBAR_CONFIGURATION("SIDEBAR_CONFIGURATION", "侧边栏配置"),
    /**
     * 屏蔽词
     */
    BLOCKING_WORDS("BLOCKING_WORDS", "屏蔽词"),

    /**
     * 系统消息
     */
    USER_SYSTEM_MESSAGES("USER_SYSTEM_MESSAGES:%s", "用户已读系统消息标识:uid"),
    SYSTEM_MESSAGES_READ_LIST("SYSTEM_MESSAGES_READ_LIST", "系统消息已读用户列表"),

    /**
     * (名言)热门作者
     */
    POPULAR_AUTHORS("POPULAR_AUTHORS", "(名言)热门作者"),
    POPULAR_LABEL("POPULAR_LABEL", "(名言)热门标签"),

    ;

    private String key;
    private String value;

    RedisBusinessKeyEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
