package com.aurora.business.mapper.news;

import com.aurora.business.domain.entity.news.NewsSystemInfo;
import com.aurora.business.domain.entity.news.NewsUserInfo;
import com.aurora.business.domain.vo.news.NewsSystemInfoVo;
import com.aurora.business.domain.vo.news.NewsUserInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统消息Mapper接口
 *
 * @author aurora
 * @date 2023-04-23
 */
@Mapper
public interface NewsSystemInfoMapper extends BaseMapperPlus<NewsSystemInfoMapper, NewsSystemInfo, NewsSystemInfoVo> {

    List<NewsSystemInfoVo> selectBaseUid(@Param("uid") String uid);

    IPage<NewsUserInfoVo> selectUid(@Param("uid") String uid, Page<NewsUserInfo> build);

    @Select("select id from b_news_system_info where state=0 and type=2")
    List<NewsSystemInfoVo> selectBase();
}

