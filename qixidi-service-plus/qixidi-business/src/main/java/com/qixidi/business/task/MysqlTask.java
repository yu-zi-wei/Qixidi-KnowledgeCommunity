package com.qixidi.business.task;

import com.qixidi.business.service.tool.SystemMysqlBackupsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mysqlz 备份定时任务
 */
@Slf4j
@Service
public class MysqlTask {

    @Autowired
    SystemMysqlBackupsService systemMysqlBackupsService;

    public void mysqlBack() {
        systemMysqlBackupsService.mysqlBackups();

    }
}
