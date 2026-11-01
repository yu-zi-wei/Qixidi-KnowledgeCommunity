package com.qixidi.business.mapper.news;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.news.NewsSystemRead;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统消息已读记录Mapper接口
 *
 * @author ziwei
 * @date 2026年11月01日
 */
@Mapper
public interface NewsSystemReadMapper extends BaseMapperPlus<NewsSystemReadMapper, NewsSystemRead, NewsSystemRead> {

    /**
     * 批量插入已读记录（INSERT IGNORE + uk_uid_news 唯一键：冲突跳过，重复标记/并发幂等）
     */
    @Insert("<script>INSERT IGNORE INTO b_news_system_read(uid, news_id, create_time) VALUES "
            + "<foreach collection='list' item='item' separator=','>(#{item.uid}, #{item.newsId}, #{item.createTime})</foreach>"
            + "</script>")
    int insertIgnoreBatch(@Param("list") List<NewsSystemRead> list);

    /**
     * 查询用户已读的系统消息id集（Redis 已读集丢失时的回灌数据源）
     */
    @Select("select news_id from b_news_system_read where uid = #{uid}")
    List<Long> selectNewsIdsByUid(@Param("uid") String uid);
}
