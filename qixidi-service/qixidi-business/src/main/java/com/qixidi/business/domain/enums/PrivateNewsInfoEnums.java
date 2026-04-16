package com.qixidi.business.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zi-wei
 * @create 2026/4/16 11:44
 */
@Getter
@AllArgsConstructor
public enum PrivateNewsInfoEnums {
    UNREAD(1, "未读"),
    READ(2, "已读"),
    ;
    private Integer code;
    private String value;
}
