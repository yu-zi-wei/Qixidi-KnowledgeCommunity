package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysOperLog;
import org.apache.ibatis.annotations.Update;

/**
 * 操作日志 数据层
 *
 * @author Lion Li
 */
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLogMapper, SysOperLog, SysOperLog> {

    /**
     * 清空操作日志。BlockAttack 防全表删除拦截器解析所有 PreparedStatement（含注解 SQL），
     * 无 WHERE 的 DELETE 一律拒绝，清空只能走 TRUNCATE（DDL 不在其解析范围）
     */
    @Update("TRUNCATE TABLE sys_oper_log")
    void cleanTable();
}
