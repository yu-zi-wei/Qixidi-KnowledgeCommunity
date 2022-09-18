package com.aurora.business.mapper.article;

import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章信息Mapper接口
 *
 * @author aurora
 * @date 2022-08-16
 */
public interface ArticleInformationMapper extends BaseMapperPlus<ArticleInformationMapper, ArticleInformation, ArticleInformationVo> {

    IPage<ArticleInformationVo> selectIndex(Page<ArticleInformation> build,  @Param(Constants.WRAPPER) QueryWrapper<ArticleInformation> wrapper);

    List<ArticleInformationVo> selectTypeSort(@Param("bo")  SortTypeBo bo);
}

