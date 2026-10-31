package com.qixidi.business.mapper.user;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.user.SignLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 前台登录日志 数据层
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Mapper
public interface SignLogMapper extends BaseMapperPlus<SignLogMapper, SignLog, SignLog> {

    /**
     * 清空前台登录日志（TRUNCATE：无 WHERE 的 delete 会被 BlockAttackInnerInterceptor 拦截）
     */
    @Update("TRUNCATE TABLE b_sign_log")
    void cleanTable();

}
