package com.aurora.business.mapper.stat;

import com.aurora.business.domain.entity.stat.StatDataInfo;
import com.aurora.business.domain.vo.stat.StatDataInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 统计数据信息Mapper接口
 *
 * @author aurora
 * @date 2023-03-14
 */
@Mapper
public interface StatDataInfoMapper extends BaseMapperPlus<StatDataInfoMapper, StatDataInfo, StatDataInfoVo> {

    Integer insertUpdate(@Param("statDataInfo") StatDataInfo statDataInfo);

    StatDataInfo selectLists(String statTime);
}

