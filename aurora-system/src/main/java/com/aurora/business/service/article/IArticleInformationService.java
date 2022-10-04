package com.aurora.business.service.article;

import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 文章信息Service接口
 *
 * @author aurora
 * @date 2022-08-16
 */
public interface IArticleInformationService {

    /**
     * 查询文章信息
     *
     * @param id 文章信息主键
     * @return 文章信息
     */
    ArticleInformationVo queryById(Long id);

    /**
     * 查询文章信息列表
     *
     * @return 文章信息集合
     */
    /**
     * 查询文章信息列表
     *
     * @param bo 文章信息
     * @return 文章信息集合
     */
    List<ArticleInformationVo> queryList(ArticleInformationBo bo);

    /**
     * 添加文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    Boolean insertByBo(ArticleInformationBo bo);

    /**
     * 修改文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    Boolean updateByBo(ArticleInformationBo bo);

    /**
     * 校验并批量删除文章信息信息
     *
     * @param ids     需要删除的文章信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<ArticleInformationVo> index(ArticleInformationBo bo, PageQuery pageQuery);

    List<ArticleInformationVo> sortIndex(SortTypeBo bo);

    List<ArticleInformationVo> articleList(ArticleInformationBo bo, PageQuery pageQuery);

    ArticleInformationVo details(Long id);

    List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery);

    R fabulousAdd(Long id);
}

