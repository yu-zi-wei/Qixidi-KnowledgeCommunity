package com.aurora.common.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    /**
     * 文件后缀
     */
    public static final String FILE_SUFFIX = ".sql";

    /**
     * 数据库的名字
     */
    public static final String DATA_BASE_NAME = "aurora_admin";

    /**
     * 判断操作系统类型、Linux|Windows
     */
    public static boolean isSystem(String osName) {
        Boolean flag = null;
        if (osName.startsWith("windows")) {
            flag = true;
        } else if (osName.startsWith("linux")) {
            flag = false;
        }
        return flag;
    }
}
