package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysNoticeMapper extends BaseMapperPlus<SysNoticeMapper, SysNotice, SysNotice> {

}
