package com.aurora.business.mapper.configure;


import com.aurora.business.domain.entity.configure.ToToolInfo;
import com.aurora.business.domain.vo.configure.ToToolInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;

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
