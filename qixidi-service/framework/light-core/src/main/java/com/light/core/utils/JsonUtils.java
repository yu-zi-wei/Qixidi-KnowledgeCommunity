package com.light.core.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.light.core.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JSON 工具类（Jackson 3：读写失败抛非受检 JacksonException，无需逐处 try/catch）
 *
 * @author 芋道源码
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static final JsonMapper JSON_MAPPER = SpringUtils.getBean(JsonMapper.class);

    public static JsonMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    public static String toJsonString(Object object) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        return JSON_MAPPER.writeValueAsString(object);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return JSON_MAPPER.readValue(text, clazz);
    }

    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        return JSON_MAPPER.readValue(bytes, clazz);
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return JSON_MAPPER.readValue(text, typeReference);
    }

    public static Dict parseMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return JSON_MAPPER.readValue(text, JSON_MAPPER.getTypeFactory().constructType(Dict.class));
    }

    public static List<Dict> parseArrayMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        return JSON_MAPPER.readValue(text, JSON_MAPPER.getTypeFactory().constructCollectionType(List.class, Dict.class));
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        return JSON_MAPPER.readValue(text, JSON_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    /**
     * obj 转list
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                if (o == null) {
                    //是否存null
                    //                    result.add(clazz.cast(null));
                    continue;
                }
                // 转换前的前置判断，避免clazz.cast的时候出现类转换异常
                if (o.getClass().equals(clazz)) {
                    // 将对应的元素进行类型转换
                    result.add(clazz.cast(o));
                }
            }
            return result;
        }
        return result;
    }

    /**
     * obj 转set
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<T> castSet(Object obj, Class<T> clazz) {
        Set<T> result = new HashSet<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                if (o == null) {
                    //是否存null
                    //                    result.add(clazz.cast(null));
                    continue;
                }
                // 转换前的前置判断，避免clazz.cast的时候出现类转换异常
                if (o.getClass().equals(clazz)) {
                    // 将对应的元素进行类型转换
                    result.add(clazz.cast(o));
                }
            }
            return result;
        }
        return result;
    }
}
