package com.aurora.business.mapper.count;

import com.aurora.business.domain.entity.count.CountUserWebsiteEntity;
import com.aurora.business.domain.vo.CountUserWebsiteVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CountUserWebsiteMapper extends BaseMapperPlus<CountUserWebsiteMapper, CountUserWebsiteEntity, CountUserWebsiteVo> {

    Integer updateAdd(@Param("uuid") String uuid, @Param("code") Integer code);

//    Integer updateAddList(@Param("ids") List<Long> uuid, @Param("code") Integer code);

    Integer updateDelete(@Param("uuid") String uuid, @Param("code") Integer code);

    CountUserWebsiteVo selectCensus(@Param("uid") String uid);

    Integer updateList(@Param("list") List<CountUserWebsiteVo> articleTask, @Param("code") Integer code);

    Integer updateExtremelyDate();

//    Integer updateDeleteList(@Param("ids") List<Long> uuid, @Param("code") Integer code);
}
