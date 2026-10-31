package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysLogininfor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysLogininforMapper extends BaseMapperPlus<SysLogininforMapper, SysLogininfor, SysLogininfor> {

    /**
     * 清空登录日志。BlockAttack 防全表删除拦截器解析所有 PreparedStatement（含注解 SQL），
     * 无 WHERE 的 DELETE 一律拒绝，清空只能走 TRUNCATE（DDL 不在其解析范围）
     */
    @Update("TRUNCATE TABLE sys_logininfor")
    void cleanTable();
}
