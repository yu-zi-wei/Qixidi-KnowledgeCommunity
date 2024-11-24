package com.aurora.business.mapper;

import com.aurora.business.domain.bo.SearchRecordsBo;
import com.aurora.business.domain.entity.SearchRecords;
import com.aurora.business.domain.vo.SearchRecordsVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 搜索记录Mapper接口
 *
 * @author aurora
 * @date 2023-04-18
 */
@Mapper
public interface SearchRecordsMapper extends BaseMapperPlus<SearchRecordsMapper, SearchRecords, SearchRecordsVo> {

    @Select("select content from b_search_records where uid=#{bo.uid} order by create_time desc")
    IPage<SearchRecordsVo> selectContent(@Param("bo") SearchRecordsBo bo, Page<SearchRecords> build);
}

