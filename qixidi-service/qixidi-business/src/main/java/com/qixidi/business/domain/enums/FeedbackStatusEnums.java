package com.qixidi.business.domain.enums;

import lombok.Getter;

/**
 * @author zi-wei
 * @create 2024/11/12 10:08
 */
@Getter
public enum FeedbackStatusEnums {
    //状态（1-待处理，2-进行中，3-已完成，4-已关闭）
    TO_BE_PROCESSED(1, "开启的"),
    UNDER_WAY(2, "进行中"),
    COMPLETED(3, "已完成"),
    CLOSED(4, "已关闭"),
    ;
    private Integer code;
    private String message;

    FeedbackStatusEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String acquireStatusMessage(Integer code) {
        for (FeedbackStatusEnums value : values()) {
            if (value.getCode().equals(code)) {
                return value.getMessage();
            }
        }
        return null;
    }
}
