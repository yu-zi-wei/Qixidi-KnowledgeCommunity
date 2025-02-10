package com.qixidi.system.runner;

import com.light.core.config.LightConfig;
import com.qixidi.system.service.ISysConfigService;
import com.qixidi.system.service.ISysDictTypeService;
import com.qixidi.system.service.ISysOssConfigService;
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
    private final ISysDictTypeService dictTypeService;
    private final ISysOssConfigService ossConfigService;


    @Override
    public void run(ApplicationArguments args) {
        ossConfigService.init();
        log.info("初始化OSS配置成功");
        if (ruoyiConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");

        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");

    }

}
