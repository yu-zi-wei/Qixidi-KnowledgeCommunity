package com.light.core.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 随机数工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomNumberUtils {

    /**
     * 生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
     *
     * @return
     */
    public static String UUIDS() {
        return IdUtil.randomUUID();
    }

    /**
     * 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
     *
     * @return
     */
    public static String UUIDSnot() {
        return IdUtil.randomUUID();
    }

    /**
     * //生成类似：5b9e306a4df4f8c54a39fb0c
     *
     * @return
     */
    public static String ObjectIds() {
        return IdUtil.objectId();
    }

    /**
     * 如果希望ID能够按照时间有序生成。Twitter的Snowflake 算法就是这种生成器。
     *
     * @return
     */
    public static Long Snowflakes() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        System.out.println(Snowflakes());
        System.out.println(ObjectIds());
    }
}
