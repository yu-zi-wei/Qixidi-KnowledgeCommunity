package com.light.core.utils;

/**
 * @author zi-wei
 * @create 2026/5/14 9:25
 */
public class EscapeUtil {
    public static String escapeChar(String before) {
        if (StringUtils.isNotEmpty(before)) {
            before = before.replaceAll("\\\\", "\\\\\\\\");
            before = before.replaceAll("_", "\\\\_");
            before = before.replaceAll("%", "\\\\%");
        }

        return before;
    }
}
