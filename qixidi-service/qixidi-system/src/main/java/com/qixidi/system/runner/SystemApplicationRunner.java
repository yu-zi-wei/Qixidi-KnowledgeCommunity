package com.qixidi.system.runner;

import com.light.core.config.LightConfig;
import com.qixidi.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author ziwei
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final LightConfig ruoyiConfig;
    private final ISysConfigService configService;

    @Override
    public void run(ApplicationArguments args) {
        if (ruoyiConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");

    }

}
