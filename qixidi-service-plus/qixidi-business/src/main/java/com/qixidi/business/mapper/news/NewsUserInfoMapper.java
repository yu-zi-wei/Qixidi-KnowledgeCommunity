package com.qixidi.business.mapper.news;

import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserInfo;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户消息Mapper接口
 *
 * @author aurora
 * @date 2022-11-03
 */
public interface NewsUserInfoMapper extends BaseMapperPlus<NewsUserInfoMapper, NewsUserInfo, NewsUserInfoVo> {

    @Select("SELECT type,state,been_read from b_news_user_info where been_read=0 and state=0 and recipient_id=#{uid}")
    List<NewsUserInfo> selectLists(@Param("uid") String uid);

    IPage<NewsUserInfoVo> userList(@Param("bo") NewsUserInfoBo bo, Page<NewsUserInfo> build);

    IPage<ArticleCommentNewsVo> selectArticleNews(@Param("uid") String uuid, @Param("type")int type,Page build);

    IPage<NewsUserInfoVo> selectFollowNews(@Param("uid")String uuid,@Param("type") Integer type, Page<Object> build);

    IPage<NewsUserInfoVo> selectFabulousNews(@Param("uid")String uuid,@Param("type") Integer type, Page<Object> build);
}

