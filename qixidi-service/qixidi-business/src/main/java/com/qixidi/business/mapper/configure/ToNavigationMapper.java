package com.qixidi.business.mapper.configure;


import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.configure.ToNavigation;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 导航栏配置Mapper接口
 *
 * @author aurora
 * @date 2022-09-16
 */
@Mapper
public interface ToNavigationMapper extends BaseMapperPlus<ToNavigationMapper, ToNavigation, ToNavigationVo> {

}

