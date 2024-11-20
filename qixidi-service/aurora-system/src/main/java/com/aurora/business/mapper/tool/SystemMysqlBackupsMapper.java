package com.aurora.business.mapper.tool;

import com.aurora.business.domain.entity.SystemMysqlBackups;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口描述： MySQL备份接口
 */
public interface SystemMysqlBackupsMapper extends BaseMapper<SystemMysqlBackups> {

    /**
     * 查询所有备份数据
     */
    List<SystemMysqlBackups> selectBackupsList();

    /**
     * 根据ID查询
     */
    SystemMysqlBackups selectListId(@Param("id") Long id);
}
