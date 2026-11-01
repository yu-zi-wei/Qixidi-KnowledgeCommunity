package com.qixidi.business.mapper.fabulous;


import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.vo.article.ArticleCountVo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 点赞Mapper接口
 *
 * @author aurora
 * @date 2022-10-01
 */
@Mapper
public interface FabulousRecordMapper extends BaseMapperPlus<FabulousRecordMapper, FabulousRecord, FabulousRecordVo> {

    List<ArticleCountVo> selectLikeCountByArticleIds(@Param("articleIds") Collection<Long> articleIds);
}

