package com.qixidi.business.mapper.comment;

import com.light.core.core.domain.vo.CensusVo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年02月26日
 */
@Mapper
public interface NewsUserRecordMapper extends BaseMapper<NewsUserRecord> {
    @Select("SELECT type,been_read,news_id from b_news_user_record where been_read=0 and uid=#{uid}")
    List<NewsUserRecord> selectLists(String uid);

    /**
     * 按天统计互动趋势（title 区分序列：fabulous=获赞 comment=评论，评论子类型 6/7 并入评论）
     */
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') as dateTimes, " +
        "CASE WHEN type = 2 THEN 'fabulous' ELSE 'comment' END as title, COUNT(*) as censusSum " +
        "FROM b_news_user_record " +
        "WHERE uid = #{uid} AND type IN (1, 2, 6, 7) AND create_time >= #{time} " +
        "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d'), CASE WHEN type = 2 THEN 'fabulous' ELSE 'comment' END " +
        "ORDER BY DATE_FORMAT(create_time, '%Y-%m-%d')")
    List<CensusVo> selectInteractTrend(@Param("uid") String uid, @Param("time") String time);
}
