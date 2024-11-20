package com.aurora.common.utils;

import cn.hutool.core.codec.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 加密工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecureUtils {
    public static void main(String[] args) {
        String str = Base64.decodeStr("ZGFua29nYWk=");
        System.out.println(str);
    }
}
