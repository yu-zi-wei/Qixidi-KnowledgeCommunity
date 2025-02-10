package com.qixidi.business.service.tool;

import com.qixidi.business.domain.entity.SystemMysqlBackups;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/9/16 15:19
 * ClassName:SystemMysqlBackupsservice
 * 类描述： MySQL备份接口
 */
@Service
public interface SystemMysqlBackupsService extends IService<SystemMysqlBackups> {

    /**
     * 查询所有备份数据
     */
    List<SystemMysqlBackups> selectBackupsList();

    /**
     * mysql备份接口
     */
    Object mysqlBackups();

    /**
     * 根据ID查询
     */
    SystemMysqlBackups selectListId(Long id);

    /**
     * 恢复数据库
     *
     * @param smb      恢复对象
     * @param userName 数据库用户名
     * @param password 数据库密码
     * @return
     */
    Object rollback(SystemMysqlBackups smb, String userName, String password);
}
