package com.qixidi.business.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态
 *
 * @author zi-wei
 * @create 2026/4/24 14:41
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnums {
    //状态（0：正常，1：已删除）
    NORMAL(0, "正常"),
    DELETE(1, "已删除"),
    ;
    private Integer code;
    private String msg;
}
