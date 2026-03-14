package com.qixidi.business.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.SearchRecords;
import com.qixidi.business.domain.vo.SearchRecordsVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索记录Mapper接口
 *
 * @author aurora
 * @date 2023-04-18
 */
@Mapper
public interface SearchRecordsMapper extends BaseMapperPlus<SearchRecordsMapper, SearchRecords, SearchRecordsVo> {

}

