package com.qixidi.business.mapper.configure;


import com.qixidi.business.domain.entity.configure.ToToolInfo;
import com.qixidi.business.domain.vo.configure.ToToolInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 工具信息Mapper接口
 *
 * @author aurora
 * @date 2022-10-21
 */
public interface ToToolInfoMapper extends BaseMapperPlus<ToToolInfoMapper, ToToolInfo, ToToolInfoVo> {

    List<ToToolInfoVo> selectListSlq();
}
