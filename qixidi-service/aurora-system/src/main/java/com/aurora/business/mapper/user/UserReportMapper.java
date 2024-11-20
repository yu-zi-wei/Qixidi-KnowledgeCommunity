package com.aurora.business.mapper.user;

import com.aurora.business.domain.entity.user.UserReport;
import com.aurora.business.domain.vo.user.UserReportVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 用户签到Mapper接口
 *
 * @author aurora
 * @date 2023-04-10
 */
public interface UserReportMapper extends BaseMapperPlus<UserReportMapper, UserReport, UserReportVo> {

    @Select("select left(report_time,10) as reportTime from user_report where uid=#{uid} ORDER BY report_time desc")
    List<String> selectReportTime(@Param("uid") String uid);

    @Select("select  COUNT(id) from user_report WHERE left(report_time,10)=#{reportTime}")
    Integer selectIsReportTime(@Param("reportTime") Date reportTime);
}
