package com.qixidi.business.domain.enums;

/**
 * 统用状态
 */
public enum StatusEnums {
    NORMAL(0, "正常"),
    DELETE(1, "已删除"),
    ;

    private Integer code;
    private String value;

    StatusEnums(Integer code, String value) {
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

