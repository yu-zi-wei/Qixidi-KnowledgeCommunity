package com.qixidi.business.mapper.comment;

import com.light.core.core.domain.vo.CensusVo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.DictumCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserRecordVo;
import com.qixidi.business.domain.vo.news.TimeNotesCommentNewsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 后台管理分页（发送者/接收者展示字段由 Service 层组装，筛选条件见 NewsUserRecordBo，列名须带 nur 别名）
     */
    IPage<NewsUserRecordVo> selectSystemPage(Page<NewsUserRecord> page, @Param(Constants.WRAPPER) QueryWrapper<NewsUserRecord> wrapper);

    @Select("SELECT type,been_read,news_id from b_news_user_record where been_read=0 and uid=#{uid}")
    List<NewsUserRecord> selectLists(String uid);

    IPage<ArticleCommentNewsVo> selectArticleNews(@Param("uid") String uuid, @Param("type") int type, Page<NewsUserRecord> build);

    IPage<TimeNotesCommentNewsVo> selectTimeNotesCommentNews(@Param("uid") String uuid, @Param("type") int type, Page<NewsUserRecord> build);

    IPage<DictumCommentNewsVo> selectDictumCommentNews(@Param("uid") String uuid, @Param("type") int type, Page<NewsUserRecord> build);

    /**
     * 关注消息分页（aliveUids=存活用户集合，等价原 join um.state=0 过滤；发送者展示字段由 Service 组装）
     */
    IPage<NewsUserInfoVo> selectFollowNews(@Param("uid") String uuid, @Param("type") Integer type,
                                           @Param("aliveUids") List<String> aliveUids, Page<NewsUserRecord> build);

    /**
     * 点赞消息分页（aliveUids=存活用户集合，等价原 join um.state=0 过滤；发送者展示字段由 Service 组装）
     */
    IPage<NewsUserInfoVo> selectFabulousNews(@Param("uid") String uuid, @Param("type") Integer type,
                                              @Param("aliveUids") List<String> aliveUids, Page<NewsUserRecord> build);

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
