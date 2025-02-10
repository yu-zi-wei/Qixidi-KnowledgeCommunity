package com.qixidi.business.runner;

import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.business.service.configure.IToSidebarService;
import com.qixidi.business.service.shield.IToShieldWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 business 模块对应业务数据
 *
 * @author zi-wei
 * @create 2025/2/10 14:56
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BusinessApplicationRunner implements ApplicationRunner {
    private final IToNavigationService iToNavigationService;
    private final IToSidebarService iToSidebarService;
    private final IToShieldWordService iToShieldWordService;


    @Override
    public void run(ApplicationArguments args) {
        iToSidebarService.sidebarList();
        log.info("侧边栏配置缓存成功");

        iToShieldWordService.ShieldWordRefresh();
        log.info("屏蔽词缓存成功");

    }
}
