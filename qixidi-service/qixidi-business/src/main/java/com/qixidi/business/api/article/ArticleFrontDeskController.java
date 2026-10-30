package com.qixidi.business.api.article;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;

import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.SortTypeBo;
import com.qixidi.business.domain.vo.article.ArticleArchiveVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【前台】文章白名单信息接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class ArticleFrontDeskController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 查询文章推荐列表
     */
    @PostMapping("/article/recommend/list")
    public TableDataInfo<ArticleInformationVo> articleRecommendList(@Validated(QueryGroup.class) @RequestBody ArticleInformationBo bo) {
        return TableDataInfo.build(iArticleInformationService.articleRecommendList(bo, bo));
    }

    /**
     * 查询文章信息列表
     */
    @PostMapping("/article/list")
    public TableDataInfo<ArticleInformationVo> articleList(@Validated(QueryGroup.class) @RequestBody ArticleInformationBo bo) {
        return TableDataInfo.build(iArticleInformationService.articleList(bo, bo));
    }

    /**
     * 查询相关文章
     *
     * @param bo
     * @param bo
     * @return
     */
    @PostMapping("/article/related/list")
    public List<ArticleInformationVo> relatedList(@RequestBody ArticleInformationBo bo) {
        return iArticleInformationService.relatedList(bo, bo);
    }

    /**
     * 文章排序
     *
     * @param bo
     * @param bo
     * @return
     */
    @PostMapping("/article/sort")
    public TableDataInfo<ArticleInformationVo> sortIndex(@RequestBody SortTypeBo bo) {
        return TableDataInfo.build(iArticleInformationService.sortIndex(bo, bo));
    }

    /**
     * 精选文章
     *
     * @return
     */
    @GetMapping("/article/selected")
    public List<ArticleInformationVo> selected() {
        return iArticleInformationService.selected();
    }

    /**
     * 查询文章详情
     *
     * @param id
     * @return
     */
    @GetMapping("/article/details/{id}")
    public ArticleInformationVo details(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return iArticleInformationService.details(Long.valueOf(id));
    }

    /**
     * 获取文章基本信息
     *
     * @param id
     * @return
     */
    @GetMapping("/article/basic/{id}")
    public ArticleInformationVo basicInfo(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return iArticleInformationService.basicInfo(Long.valueOf(id));
    }

    /**
     * 查询用户文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @PostMapping("/article/user/list")
    public TableDataInfo<ArticleInformationVo> getArticleInfo(@RequestBody ArticleInformationBo bo) {
        return TableDataInfo.build(iArticleInformationService.getArticleInfo(bo, bo));
    }

    /**
     * 查询用户文章标题列表
     *
     * @param bo
     * @param bo
     * @return
     */
    @PostMapping("/article/title/list")
    public List<ArticleInformationVo> getArticleInfoList(@RequestBody ArticleInformationBo bo) {
        return iArticleInformationService.getArticleInfoList(bo, bo);
    }

    /**
     * 查询用户关注文章列表
     *
     * @param bo
     * @param bo
     * @return
     */
    @PostMapping("/article/follow/list")
    public TableDataInfo<ArticleInformationVo> FollowArticleInfoList(@RequestBody SortTypeBo bo) {
        return TableDataInfo.build(iArticleInformationService.FollowArticleInfoList(bo, bo));
    }

    /**
     * 查询标签分类文章列表
     *
     * @param bo
     * @param bo
     * @return
     */
    @PostMapping("/article/label/list")
    public TableDataInfo<ArticleInformationVo> LabelGArticleInfoList(@RequestBody SortTypeBo bo) {
        return TableDataInfo.build(iArticleInformationService.LabelGArticleInfoList(bo, bo));
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
    public void addArticleBrowse(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id,
                                 @PathVariable("label") String label,
                                 HttpServletRequest request) {
        iArticleInformationService.addArticleBrowse(id, label, request);
    }

    /**
     * 文章归档
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/article/archive")
    public TableDataInfo<ArticleArchiveVo> articleArchive(@RequestBody PageQuery pageQuery) {
        return iArticleInformationService.articleArchive(pageQuery);
    }
}
