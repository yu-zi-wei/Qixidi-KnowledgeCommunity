package com.qixidi.business.mapper.user;

import com.qixidi.business.domain.entity.user.BrowsingHistory;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 用户浏览历史
 * <p>
 * Mapper接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface BrowsingHistoryMapper extends BaseMapperPlus<BrowsingHistoryMapper, BrowsingHistory, BrowsingHistoryVo> {

    BrowsingHistoryVo selectIsTime(@Param("uuid") String uuid, @Param("targetType") Integer targetType, @Param("targetId") Long targetId, @Param("hour") String hour);

    int insertOrUpdates(@Param("add") BrowsingHistory add);
}
