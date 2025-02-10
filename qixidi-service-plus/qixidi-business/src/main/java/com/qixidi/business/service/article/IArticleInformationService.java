package com.qixidi.business.service.article;

import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.bo.article.SortTypeBo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import jakarta.servlet.http.HttpServletRequest;
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
    ArticleInformationVo insertByBo(ArticleInformationBo bo);

    /**
     * 修改文章信息
     *
     * @param bo 文章信息n
     * @return 结果
     */
    ArticleInformationVo updateByBo(ArticleInformationBo bo);

    /**
     * 校验并批量删除文章信息信息
     *
     * @param ids     需要删除的文章信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<ArticleInformationVo> index(ArticleInformationBo bo, PageQuery pageQuery);

    IPage<ArticleInformationVo> sortIndex(SortTypeBo bo,PageQuery pageQuery);

    IPage<ArticleInformationVo> articleList(ArticleInformationBo bo, PageQuery pageQuery);

    ArticleInformationVo details(Long id);

    List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery);

    ArticleInformationVo getArtticle(Long valueOf);

    ArticleInformationVo basicInfo(Long valueOf);

    Page<ArticleInformationVo> getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery);

    int delete(Long id);

    ArticleInformationVo saveDraft(ArticleInformationTwoBo bo);

    List<ArticleInformationVo> getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery);

    List<ArticleInformationVo> selected();

    Boolean addArticleBrowse(Long id,String label, HttpServletRequest request);

    List<CensusVo> timeArticleCensus(CensusEntity bo);

    Page<ArticleInformationVo> FollowArticleInfoList(SortTypeBo bo, PageQuery pageQuery);

    Page<ArticleInformationVo> LabelGArticleInfoList(SortTypeBo bo, PageQuery pageQuery);

    IPage<ArticleInformationVo> articleRecommendList(ArticleInformationBo bo, PageQuery pageQuery);

    List<ArticleInformationVo> latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery);
}

