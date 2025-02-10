package com.qixidi.business.mapper.collection;

import com.qixidi.business.domain.entity.collection.CollectionRecord;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.collection.CollectionInformationVo;
import com.qixidi.business.domain.vo.collection.CollectionRecordVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 文章收藏关联Mapper接口
 *
 * @author aurora
 * @date 2022-11-10
 */
@Mapper
public interface CollectionRecordMapper extends BaseMapperPlus<CollectionRecordMapper, CollectionRecord, CollectionRecordVo> {

    @Select("SELECT uid AS uuid, count( uid ) AS collectionCount FROM `b_collection_record` WHERE state = 0 GROUP BY uid")
    List<CountUserWebsiteVo> selectCollectionTask();

    @Select("select collection_id as id, count(collection_id) as includedCount from b_collection_record where uid=#{uid} and state=0 group by collection_id")
    List<CollectionInformationVo> selectGroupOn(@Param("uid") String uid);
}
