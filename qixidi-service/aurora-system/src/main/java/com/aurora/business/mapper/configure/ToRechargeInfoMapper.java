package com.aurora.business.mapper.configure;

import com.aurora.business.domain.entity.configure.ToRechargeInfo;
import com.aurora.business.domain.vo.configure.ToRechargeInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
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
