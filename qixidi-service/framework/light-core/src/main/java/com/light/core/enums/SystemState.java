package com.light.core.enums;

public enum SystemState {
    NORMAL_STATE(0, "正常"),
    INVALID_STATE(1, "已失效");;
    private Integer code;
    private String value;

    SystemState(Integer code, String value) {
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
