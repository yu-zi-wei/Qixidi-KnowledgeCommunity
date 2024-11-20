package com.aurora.common.enums;

public enum UserFollowType {
    USER_FOLLOW(1, "用户关注"),
    LABEL_FOLLOW(2, "标签关注"),
    ACTIVITY_FOLLOW(3, "活动关注"),
    CIRCLE_FOLLOW(4, "圈子关注"),
    ;
    private Integer code;
    private String value;

    UserFollowType(Integer code, String value) {
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
