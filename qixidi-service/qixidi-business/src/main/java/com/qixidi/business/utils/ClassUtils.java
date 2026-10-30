package com.qixidi.business.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class ClassUtils {
    public static Map<String, Object> reflect(Object e) {
        Map<String, Object> map = new HashMap<>();
        // 沿继承链收集：VO 继承实体后业务字段在父类，getDeclaredFields 只取当前类会漏掉
        for (Class<?> cls = e.getClass(); cls != null && cls != Object.class; cls = cls.getSuperclass()) {
            for (Field f : cls.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic()) {
                    continue;
                }
                f.setAccessible(true);
                try {
                    map.put(f.getName(), f.get(e));
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
        }
        return map;
    }

}
