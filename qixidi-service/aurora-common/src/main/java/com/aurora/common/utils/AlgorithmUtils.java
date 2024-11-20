package com.aurora.common.utils;

public class AlgorithmUtils {

    /**
     * 默认范围 （一年）
     */
    private static final Integer scopes = 365;

    /**
     * 输入值越大返回值越小，相反一样
     *
     * @param scope 范围
     * @param code  输入值
     * @return
     */
    public static Integer directionExport(Integer scope, Integer code) {
        if (code >= scope) return 1;
        if (code.equals(0)) return scope;
        int[] nums = new int[scope];
        for (int i = 0; i < scope; i++) {
            nums[scope - i - 1] = i;
        }
        return nums[code - 1];
    }

    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            Integer SUN = directionExport(scopes, i);
            System.out.printf("SUN: " + SUN + "==>" + i + "\n");
            System.out.printf("SUN/scopes: " + SUN * 0.01 + "\n");
        }
    }

    public static Double directionExport(Integer code) {
        return directionExport(scopes, code) * 0.01;
    }

}
