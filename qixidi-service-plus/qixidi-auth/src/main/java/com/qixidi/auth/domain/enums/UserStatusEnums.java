package com.qixidi.auth.domain.enums;

/**
 * 用户状态
 *
 * @author ziwei
 */
public enum UserStatusEnums {
    OK("0", "正常"),
    DISABLE("1", "停用"),
    DELETED("2", "删除"),
    GENERAL_USER("1", "普通用户"),
    VIP_USER("2", "vip用户");

    private final String code;
    private final String info;

    UserStatusEnums(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public Long getLogCode() {
        return Long.valueOf(code);
    }

    public Integer getIntegerCode() {
        return Integer.valueOf(code);
    }

    public String getInfo() {
        return info;
    }
}
