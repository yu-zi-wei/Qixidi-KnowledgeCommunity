package com.qixidi.business.mapper.stat;

import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.business.domain.entity.stat.StatTheData;
import com.qixidi.business.domain.vo.stat.StatTheDataVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 网站数据月统计Mapper接口
 *
 * @author aurora
 * @date 2023-03-14
 */
@Mapper
public interface StatTheDataMapper extends BaseMapperPlus<StatTheDataMapper, StatTheData, StatTheDataVo> {

    int insertLists( @Param("statDataInfo") StatDataInfo statDataInfo);

    List<StatTheDataVo> selectVoLists();

}

