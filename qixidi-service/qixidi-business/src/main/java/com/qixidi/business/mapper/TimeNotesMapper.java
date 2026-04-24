package com.qixidi.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zi-wei
 * @create 2025/3/26 10:09
 */
@Mapper
public interface TimeNotesMapper extends BaseMapper<TimeNotes> {

    List<CountUserWebsiteVo> selectTimeNotesCountByUserIds(@Param("uuids") java.util.Collection<String> uuids);

}
