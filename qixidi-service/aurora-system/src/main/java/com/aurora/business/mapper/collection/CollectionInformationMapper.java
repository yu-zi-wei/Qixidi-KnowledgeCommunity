package com.aurora.business.mapper.collection;

import com.aurora.business.domain.bo.collection.CollectionRecordBo;
import com.aurora.business.domain.entity.collection.CollectionInformation;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.domain.vo.collection.CollectionInformationVo;
import com.aurora.common.core.domain.CensusEntity;
import com.aurora.common.core.domain.vo.CensusVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏夹信息Mapper接口
 *
 * @author aurora
 * @date 2022-09-29
 */
public interface CollectionInformationMapper extends BaseMapperPlus<CollectionInformationMapper, CollectionInformation, CollectionInformationVo> {

    Page<ArticleInformationVo> articleList(@Param("bo") CollectionRecordBo bo, Page buildy);

    Page<CollectionInformationVo> selectUserName(Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<CollectionInformation> lqw);

    int updateDelete(@Param("collectionId") Long collectionId);

    int updateAdd(@Param("collectionId") Long collectionId);

    List<CollectionInformationVo> selectCensus(@Param("bo") CensusEntity bo);

    List<CensusVo> timeCollectionCensus(@Param("bo") CensusEntity bo);

//    CollectionInformationVo selectInfo(Log id);
}

