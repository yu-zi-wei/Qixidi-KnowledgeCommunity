package com.aurora.web.controller.frontDesk.article;


import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.ArticleInformationTwoBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.helper.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@Api(value = "文章信息控制器", tags = {"文章信息接口"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserArticleController extends BaseController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 新增文章信息
     */
    @ApiOperation("新增文章信息")
    @Log(title = "文章信息（新增文章信息）", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/insert/article")
    public R<ArticleInformationVo> add(@Validated(AddGroup.class) @RequestBody ArticleInformationBo bo) {
        if (bo.getId() != null) {
            ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
            if (vo.getId() < -10) return R.ok(vo);
            return vo.getId() > 0 ? R.ok(vo) : R.fail();
        }
        bo.setAuditState(1);
        ArticleInformationVo vo = iArticleInformationService.insertByBo(bo);
        if (vo.getId() < -10) return R.ok(vo);
        return vo.getId() > 0 ? R.ok(vo) : R.fail();
    }

    /**
     * 更新文章信息
     */
    @ApiOperation("更新文章信息")
    @Log(title = "文章信息（更新文章信息）", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/article")
    public R<ArticleInformationVo> edit(@Validated(EditGroup.class) @RequestBody ArticleInformationBo bo) {
        ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
        return vo.getId() > 0 ? R.ok(vo) : R.fail();
    }

    /**
     * 保存草稿
     */
    @ApiOperation("保存草稿")
    @Log(title = "文章信息（保存草稿）", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/save/draft")
    public R<ArticleInformationVo> saveDraft(@Validated(EditGroup.class) @RequestBody ArticleInformationTwoBo bo) {
        ArticleInformationVo vo = iArticleInformationService.saveDraft(bo);
        return vo.getId() > 0 ? R.ok(vo) : R.fail();
    }

    @ApiOperation("查询用户文章列表")
    @GetMapping("/article/list")
    public R getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
        return R.ok(iArticleInformationService.getArticleInfo(bo, pageQuery));
    }

    @ApiOperation("查询用户文章标题列表")
    @GetMapping("/article/title/list")
    public R getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
        return R.ok(iArticleInformationService.getArticleInfoList(bo, pageQuery));
    }


    @ApiOperation("文章编辑详情")
    @GetMapping("/get/article/{id}")
    public R getArticle(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.getArtticle(Long.valueOf(id)));
    }

    @ApiOperation("删除用户文章")
    @DeleteMapping("/delete/article/{id}")
    public R delete(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id) {
        return toAjax(iArticleInformationService.delete(id));
    }

    @ApiOperation("获取用户最近文章列表")
    @GetMapping("/lately/article/list")
    public R latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
        return R.ok(iArticleInformationService.latelyArticleList(bo,pageQuery));
    }

}
