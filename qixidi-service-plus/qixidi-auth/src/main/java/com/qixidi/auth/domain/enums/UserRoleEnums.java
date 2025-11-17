package com.qixidi.auth.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 用户角色枚举
 *
 * @author zi-wei
 * @create 2025/11/14 15:06
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnums {
    GENERAL_USER(1, "普通用户"),
    CREATOR(2, "创作者"),
    ADMINISTRATOR(3, "管理员"),
    ;

    private final long code;
    private final String info;


    public static List<Long> getAdvancedRoleList() {
        return List.of(CREATOR.getCode(), ADMINISTRATOR.getCode());
    }

    public static String getRoleInfo(long code) {
        for (UserRoleEnums value : values()) {
            if (value.getCode() == code) {
                return value.getInfo();
            }
        }
        return "";
    }

}
