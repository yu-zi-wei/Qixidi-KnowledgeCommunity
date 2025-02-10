package com.qixidi.business.mapper.configure;

import com.qixidi.business.domain.entity.configure.ToRechargeInfo;
import com.qixidi.business.domain.vo.configure.ToRechargeInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 充值信息Mapper接口
 *
 * @author aurora
 * @date 2023-04-04
 */
@Mapper
public interface ToRechargeInfoMapper extends BaseMapperPlus<ToRechargeInfoMapper, ToRechargeInfo, ToRechargeInfoVo> {

}
