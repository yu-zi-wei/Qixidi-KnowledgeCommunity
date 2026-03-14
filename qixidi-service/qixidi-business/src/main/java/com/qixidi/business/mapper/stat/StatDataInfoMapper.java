package com.qixidi.business.mapper.stat;

import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
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

