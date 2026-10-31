package com.qixidi.business.mapper.user;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.user.SignLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 前台登录日志 数据层
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Mapper
public interface SignLogMapper extends BaseMapperPlus<SignLogMapper, SignLog, SignLog> {

}
