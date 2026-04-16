package com.qixidi.business.service.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

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
     * 查询用户消息
     */
    NewsUserInfoVo queryById(Long id);

    /**
     * 查询用户消息列表
     */
    TableDataInfo<NewsUserInfoVo> queryPageList(NewsUserInfoBo bo, PageQuery pageQuery);

    /**
     * 查询用户消息列表
     */
    List<NewsUserInfoVo> queryList(NewsUserInfoBo bo);

    /**
     * 新增用户消息
     */
    Boolean insertByBo(NewsUserInfoBo bo);

    /**
     * 修改用户消息
     */
    Boolean updateByBo(NewsUserInfoBo bo);

    /**
     * 校验并批量删除用户消息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<NewsUserSumVo> listSum();

    List<NewsUserSumVo> listSums(String uid);

    /** 评论消息列表 */
    TableDataInfo<ArticleCommentNewsVo> commentList(PageQuery pageQuery);

    /** 点赞消息列表 */
    TableDataInfo<NewsUserInfoVo> fabulousList(PageQuery pageQuery);

    /** 关注消息列表 */
    TableDataInfo<NewsUserInfoVo> followList(PageQuery pageQuery);

    /** 系统消息列表 */
    TableDataInfo<NewsUserInfoVo> systemList(PageQuery pageQuery);

    List<NewsUserSumVo> pushOne(String userid);

    boolean newsRead(NewsUserRecord bo);

    List<NewsUserSumVo> listInfo();

}

