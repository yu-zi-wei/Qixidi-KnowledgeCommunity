package com.qixidi.business.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {
    public static Map<String, Object> reflect(Object e) {
        Class<? extends Object> cls = e.getClass();
        Field[] fields = cls.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            try {
                map.put(f.getName(), f.get(e));
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return map;
    }

}
