package com.aurora.framework.runner;

import com.aurora.framework.manager.SysBlackListManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FrameworkApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SysBlackListManager.inst().loadData();
        log.info("黑名单列表加载完成");
    }
}
