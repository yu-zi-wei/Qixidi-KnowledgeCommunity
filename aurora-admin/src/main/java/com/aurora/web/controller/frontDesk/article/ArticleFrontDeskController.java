package com.aurora.web.controller.frontDesk.article;


import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public TableDataInfo<ArticleInformationVo> index(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.index(bo, pageQuery);
    }

    @ApiOperation("文章排序")
    @GetMapping("/sort")
    public List<ArticleInformationVo> sortIndex(SortTypeBo bo) {
        return iArticleInformationService.sortIndex(bo);
    }

}
