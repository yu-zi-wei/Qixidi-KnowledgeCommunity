package com.qixidi.business.mapper.configure;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.configure.ToSidebar;
import com.qixidi.business.domain.vo.configure.ToSidebarVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 侧边栏配置Mapper接口
 *
 * @author aurora
 * @date 2022-09-16
 */
@Mapper
public interface ToSidebarMapper extends BaseMapperPlus<ToSidebarMapper, ToSidebar, ToSidebarVo> {

}
