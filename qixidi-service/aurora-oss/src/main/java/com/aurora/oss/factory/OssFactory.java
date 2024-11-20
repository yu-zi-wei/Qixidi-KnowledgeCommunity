package com.aurora.oss.factory;

import com.aurora.common.utils.JsonUtils;
import com.aurora.common.utils.StringUtils;
import com.aurora.common.utils.redis.RedisUtils;
import com.aurora.common.utils.spring.SpringUtils;
import com.aurora.oss.constant.OssConstant;
import com.aurora.oss.enumd.OssEnumd;
import com.aurora.oss.exception.OssException;
import com.aurora.oss.properties.OssProperties;
import com.aurora.oss.service.IOssStrategy;
import com.aurora.oss.service.abstractd.AbstractOssStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传Factory
 *
 * @author Lion Li
 */
@Slf4j
public class OssFactory {

    /**
     * 初始化工厂
     */
    public static void init() {
        log.info("初始化OSS工厂");
        RedisUtils.subscribe(OssConstant.CACHE_CONFIG_KEY, String.class, type -> {
            AbstractOssStrategy strategy = getStrategy(type);
            // 未初始化不处理
            if (strategy.isInit) {
                refresh(type);
                log.info("订阅刷新OSS配置 => " + type);
            }
        });
    }

    /**
     * 获取默认实例
     */
    public static IOssStrategy instance() {
        // 获取redis 默认类型
        String type = RedisUtils.getCacheObject(OssConstant.CACHE_CONFIG_KEY);
        if (StringUtils.isEmpty(type)) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        return instance(type);
    }

    /**
     * 根据类型获取实例
     */
    public static IOssStrategy instance(String type) {
        OssEnumd enumd = OssEnumd.find(type);
        if (enumd == null) {
            throw new OssException("文件存储服务类型无法找到!");
        }
        AbstractOssStrategy strategy = getStrategy(type);
        if (!strategy.isInit) {
            refresh(type);
        }
        return strategy;
    }

    private static void refresh(String type) {
        Object json = RedisUtils.getCacheObject(OssConstant.SYS_OSS_KEY + type);
        OssProperties properties = JsonUtils.parseObject(json.toString(), OssProperties.class);
        if (properties == null) {
            throw new OssException("系统异常, '" + type + "'配置信息不存在!");
        }
        getStrategy(type).init(properties);
    }

    private static AbstractOssStrategy getStrategy(String type) {
        OssEnumd enumd = OssEnumd.find(type);
        return (AbstractOssStrategy) SpringUtils.getBean(enumd.getBeanClass());
    }

}
