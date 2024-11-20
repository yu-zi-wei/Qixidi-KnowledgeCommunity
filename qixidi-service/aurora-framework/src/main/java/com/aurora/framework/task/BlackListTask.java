package com.aurora.framework.task;

import com.aurora.framework.manager.SysBlackListManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@Component
public class BlackListTask {

    @Scheduled(cron = "0 * * * * *") // 每分钟执行一次
    public void syncBlackList() {
        SysBlackListManager.inst().blackListLokuro();
    }
}
