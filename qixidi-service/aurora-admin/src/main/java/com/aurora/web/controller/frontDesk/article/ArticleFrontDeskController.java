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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Api(value = "文章信息控制器", tags = {"文章信息接口"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class ArticleFrontDeskController extends BaseController {

    private final IArticleInformationService iArticleInformationService;


    /**
     * 查询文章推荐列表
     */
    @ApiOperation("查询文章推荐列表")
    @GetMapping("/article/recommend/list")
    public TableDataInfo<ArticleInformationVo> articleRecommendList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.articleRecommendList(bo, pageQuery));
    }

   /**
     * 查询文章信息列表
     */
    @ApiOperation("查询文章信息列表")
    @GetMapping("/article/list")
    public TableDataInfo<ArticleInformationVo> articleList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.articleList(bo, pageQuery));
    }

    @ApiOperation("查询相关文章")
    @GetMapping("/article/related/list")
    public List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.relatedList(bo, pageQuery);
    }

    @ApiOperation("文章排序")
    @GetMapping("/article/sort")
    public TableDataInfo<ArticleInformationVo> sortIndex(SortTypeBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iArticleInformationService.sortIndex(bo, pageQuery));
    }

    @ApiOperation("精选文章")
    @GetMapping("/article/selected")
    public R<List<ArticleInformationVo>> selected() {
        return R.ok(iArticleInformationService.selected());
    }

    @ApiOperation("查询文章详情")
    @GetMapping("/article/details/{id}")
    public R details(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.details(Long.valueOf(id)));
    }

    @ApiOperation("获取文章基本信息")
    @GetMapping("/article/basic/{id}")
    public R basicInfo(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.basicInfo(Long.valueOf(id)));
    }

    @ApiOperation("查询用户文章列表")
    @GetMapping("/article/user/list")
    public R getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.getArticleInfo(bo, pageQuery));
    }

    @ApiOperation("查询用户文章标题列表")
    @GetMapping("/article/title/list")
    public R getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.getArticleInfoList(bo, pageQuery));
    }

    @ApiOperation("查询用户关注文章列表")
    @GetMapping("/article/follow/list")
    public R FollowArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.FollowArticleInfoList(bo, pageQuery));
    }

    @ApiOperation("查询标签分类文章列表")
    @GetMapping("/article/label/list")
    public R LabelGArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        return R.ok(iArticleInformationService.LabelGArticleInfoList(bo, pageQuery));
    }

    @ApiOperation("文章浏览数加一")
    @GetMapping("/article/add/browse-count/{id}/{label}")
    public R addArticleBrowse(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id,
                              @PathVariable("label") String label,
                              HttpServletRequest request) {
        return toAjax(iArticleInformationService.addArticleBrowse(id, label, request));
    }

}
