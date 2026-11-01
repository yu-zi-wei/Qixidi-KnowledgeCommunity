package com.qixidi.business.service.news;

import com.light.core.core.domain.vo.CensusVo;
import com.qixidi.business.domain.bo.news.NewsUserRecordBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.DictumCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserRecordVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.domain.vo.news.TimeNotesCommentNewsVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户消息Service接口
 *
 * @author aurora
 * @date 2022-11-03
 */
public interface INewsUserInfoService {

    /**
     * 后台管理：用户消息记录分页（b_news_user_record，join 发送者/接收者昵称头像，支持类型多选与用户名搜索）
     */
    TableDataInfo<NewsUserRecordVo> recordPage(NewsUserRecordBo bo, PageQuery pageQuery);

    /**
     * 后台管理：批量删除用户消息记录
     */
    Boolean deleteRecordByIds(Collection<Long> ids);

    List<NewsUserSumVo> listSum();

    List<NewsUserSumVo> listSums(String uid);

    /** 评论消息列表 */
    TableDataInfo<ArticleCommentNewsVo> commentList(PageQuery pageQuery);

    /** 小记评论消息列表 */
    TableDataInfo<TimeNotesCommentNewsVo> timeNotesCommentList(PageQuery pageQuery);

    /** 随笔评论消息列表 */
    TableDataInfo<DictumCommentNewsVo> dictumCommentList(PageQuery pageQuery);

    /** 点赞消息列表 */
    TableDataInfo<NewsUserInfoVo> fabulousList(PageQuery pageQuery);

    /** 关注消息列表 */
    TableDataInfo<NewsUserInfoVo> followList(PageQuery pageQuery);

    /** 系统消息列表 */
    TableDataInfo<NewsUserInfoVo> systemList(PageQuery pageQuery);

    List<NewsUserSumVo> pushOne(String userid);

    boolean newsRead(NewsUserRecord bo);

    List<NewsUserSumVo> listInfo();

    /** 近30天互动趋势（获赞/评论按天聚合，title 区分序列） */
    List<CensusVo> interactTrend();

}

