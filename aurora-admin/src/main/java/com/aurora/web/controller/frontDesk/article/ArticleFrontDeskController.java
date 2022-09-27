package com.aurora.web.controller.frontDesk.article;


import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.validate.QueryGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Api(value = "文章信息控制器", tags = {"文章信息接口"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/article")
public class ArticleFrontDeskController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 查询文章信息列表
     */
    @ApiOperation("查询文章信息列表")
    @GetMapping("/list")
    public List<ArticleInformationVo> articleList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.articleList(bo, pageQuery);
    }

    @ApiOperation("查询相关文章")
    @GetMapping("/related/list")
    public List<ArticleInformationVo> relatedList(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.relatedList(bo, pageQuery);
    }

    @ApiOperation("文章排序")
    @GetMapping("/sort")
    public List<ArticleInformationVo> sortIndex(SortTypeBo bo) {
        return iArticleInformationService.sortIndex(bo);
    }

    @ApiOperation("查询文章详情")
    @GetMapping("/details/{id}")
    public ArticleInformationVo details(@NotNull(message = "id不能为空")
                                        @PathVariable("id") String id) {
        return iArticleInformationService.details(Long.valueOf(id));
    }

}
