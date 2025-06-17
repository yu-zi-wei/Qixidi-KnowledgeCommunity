package com.qixidi.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统用状态
 */
@Getter
@AllArgsConstructor
public enum StatusEnums {
    NORMAL(0, "正常"),
    DELETE(1, "已删除"),
    ;

    private Integer code;
    private String value;
}

