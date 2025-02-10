package com.qixidi.business.mapper.fabulous;


import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

