package com.aurora.business.mapper.comment;

import com.aurora.business.domain.entity.news.NewsUserRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年02月26日
 */
@Mapper
public interface NewsUserRecordMapper extends BaseMapper<NewsUserRecord> {
    @Select("SELECT type,been_read,news_id from news_user_record where been_read=0 and uid=#{uid}")
    List<NewsUserRecord> selectLists(String uid);
}
