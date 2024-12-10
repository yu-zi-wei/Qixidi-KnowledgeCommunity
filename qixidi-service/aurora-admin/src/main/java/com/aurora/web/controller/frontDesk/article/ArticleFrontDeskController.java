package com.aurora.web.controller.frontDesk.article;


import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【前台】文章白名单信息接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class ArticleFrontDeskController extends BaseController {

    private final IArticleInformationService iArticleInformationService;


    /**
     * 查询文章推荐列表
     */
    @GetMapping("/article/recommend/list")
    public TableDataInfo<ArticleInformationVo> articleRecommendList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.articleRecommendList(bo, pageQuery));
    }

    /**
     * 查询文章信息列表
     */
    @GetMapping("/article/list")
    public TableDataInfo<ArticleInformationVo> articleList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.articleList(bo, pageQuery));
    }

    /**
     * 查询相关文章
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/related/list")
    public List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.relatedList(bo, pageQuery);
    }

    /**
     * 文章排序
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/sort")
    public TableDataInfo<ArticleInformationVo> sortIndex(SortTypeBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.sortIndex(bo, pageQuery));
    }

    /**
     * 精选文章
     *
     * @return
     */
    @GetMapping("/article/selected")
    public R<List<ArticleInformationVo>> selected() {
        return R.ok(iArticleInformationService.selected());
    }

    /**
     * 查询文章详情
     *
     * @param id
     * @return
     */
    @GetMapping("/article/details/{id}")
    public R details(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.details(Long.valueOf(id)));
    }

    /**
     * 获取文章基本信息
     *
     * @param id
     * @return
     */
    @GetMapping("/article/basic/{id}")
    public R basicInfo(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.basicInfo(Long.valueOf(id)));
    }

    /**
     * 查询用户文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/user/list")
    public R getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.getArticleInfo(bo, pageQuery));
    }

    /**
     * 查询用户文章标题列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/title/list")
    public R getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.getArticleInfoList(bo, pageQuery));
    }

    /**
     * 查询用户关注文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/follow/list")
    public R FollowArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.FollowArticleInfoList(bo, pageQuery));
    }

    /**
     * 查询标签分类文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/label/list")
    public R LabelGArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.LabelGArticleInfoList(bo, pageQuery));
    }

    /**
     * 文章浏览数加一
     *
     * @param id
     * @param label
     * @param request
     * @return
     */
    @GetMapping("/article/add/browse-count/{id}/{label}")
    public R addArticleBrowse(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id,
                              @PathVariable("label") String label,
                              HttpServletRequest request) {
        return toAjax(iArticleInformationService.addArticleBrowse(id, label, request));
    }

}
