package com.light.core.utils.context;

import cn.hutool.core.date.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;

import java.lang.annotation.Annotation;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
public class SpringContext {
    private static final Object syncObj = new Object();
    private static SpringContext _inst;

    public static SpringContext inst() {
        if (_inst == null) {
            synchronized (syncObj) {
                if (_inst == null)
                    _inst = new SpringContext();
            }
        }
        return _inst;
    }

    private static ApplicationContext applicationContext;
    private String host;

    private SpringContext() {
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

    public Object getBean(String name) {
        if (!applicationContext.containsBean(name)) return null;
        return applicationContext.getBean(name);
    }

    public <T> T getBean(Class<T> clazz) {
        if (clazz == null) throw new RuntimeException("class cannot be null");
        return applicationContext.getBean(clazz);
    }

    public <T> T getBean(Class<T> clazz, String name) {
        if (!applicationContext.containsBean(name)) return null;
        return applicationContext.getBean(name, clazz);
    }

    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> clazz) {
        return applicationContext.getBeanNamesForAnnotation(clazz);
    }

    public <T> Map<String, T> getInterfaceBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public boolean isDevMode() {
        return isDevMode(true);
    }

    public boolean isDevMode(boolean onlyDev) {
        if (applicationContext == null) throw new RuntimeException("environment not set properly!");
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            if (activeProfile.contains("dev")) return true;
            if (activeProfile.contains("test") && !onlyDev) return true;
        }
        return false;
    }

    public String getSystemStartDate() {
        long startupDate = applicationContext.getStartupDate();
        Date date = new Date(startupDate);
        return DateUtil.formatDate(date);
    }

    public Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    public String getProperty(String key) {
        return applicationContext.getEnvironment().getProperty(key);
    }

    public void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    public boolean isAuditMode() {
        String auditMode = applicationContext.getEnvironment().getProperty("auditMode");
        return auditMode != null && !auditMode.equalsIgnoreCase("false");
    }

    public String getHost() {
        return host;
    }

}
