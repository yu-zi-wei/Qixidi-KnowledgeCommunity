package com.aurora.common.enums;

public enum PaymentStateEnums {
    TO_BE_PAID(1, "待支付"),
    PAYMENT_COMPLETED(2, "支付完成"),
    TO_EXPIRED(3, "已过期"),
    PAYMENT_FAILED(4, "支付失败"),
    ;

    private Integer code;
    private String value;

    PaymentStateEnums(Integer code, String value) {
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
