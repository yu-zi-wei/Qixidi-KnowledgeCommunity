package com.aurora.system.runner;

import com.aurora.business.service.configure.IToNavigationService;
import com.aurora.business.service.configure.IToSidebarService;
import com.aurora.business.service.shield.IToShieldWordService;
import com.aurora.common.config.RuoYiConfig;
import com.aurora.system.service.ISysConfigService;
import com.aurora.system.service.ISysDictTypeService;
import com.aurora.system.service.ISysOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final RuoYiConfig ruoyiConfig;
    private final ISysConfigService configService;
    private final ISysDictTypeService dictTypeService;
    private final ISysOssConfigService ossConfigService;
    private final IToNavigationService iToNavigationService;
    private final IToSidebarService iToSidebarService;
    private final IToShieldWordService iToShieldWordService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ossConfigService.init();
        log.info("初始化OSS配置成功");
        if (ruoyiConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");

        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");

        iToSidebarService.sidebarList();
        log.info("侧边栏配置缓存成功");

        iToShieldWordService.ShieldWordRefresh();
        log.info("屏蔽词缓存成功");

    }

}
