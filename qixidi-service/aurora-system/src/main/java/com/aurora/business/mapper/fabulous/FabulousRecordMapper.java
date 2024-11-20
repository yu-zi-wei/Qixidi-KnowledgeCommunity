package com.aurora.business.mapper.fabulous;


import com.aurora.business.domain.bo.user.UserHomeBo;
import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.entity.fabulous.FabulousRecord;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.domain.vo.fabulous.FabulousRecordVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞Mapper接口
 *
 * @author aurora
 * @date 2022-10-01
 */
@Mapper
public interface FabulousRecordMapper extends BaseMapperPlus<FabulousRecordMapper, FabulousRecord, FabulousRecordVo> {

    IPage<ArticleInformationVo> fabulousArticleList(@Param("bo") UserHomeBo bo, Page<ArticleInformation> build);
}

