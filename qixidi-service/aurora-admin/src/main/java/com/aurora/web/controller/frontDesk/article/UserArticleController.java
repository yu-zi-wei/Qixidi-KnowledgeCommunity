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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 【前台】文章信息接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserArticleController extends BaseController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 新增文章信息
     */
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
    @Log(title = "文章信息（保存草稿）", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/save/draft")
    public R<ArticleInformationVo> saveDraft(@Validated(EditGroup.class) @RequestBody ArticleInformationTwoBo bo) {
        ArticleInformationVo vo = iArticleInformationService.saveDraft(bo);
        return vo.getId() > 0 ? R.ok(vo) : R.fail();
    }

    /**
     * 查询用户文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/list")
    public R getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
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
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
        return R.ok(iArticleInformationService.getArticleInfoList(bo, pageQuery));
    }

    /**
     * 文章编辑详情
     *
     * @param id
     * @return
     */
    @GetMapping("/get/article/{id}")
    public R getArticle(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return R.ok(iArticleInformationService.getArtticle(Long.valueOf(id)));
    }

    /**
     * 删除用户文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/article/{id}")
    public R delete(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id) {
        return toAjax(iArticleInformationService.delete(id));
    }

    /**
     * 获取用户最近文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/lately/article/list")
    public R latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("登录已过期");
        bo.setUserId(uuid);
        return R.ok(iArticleInformationService.latelyArticleList(bo, pageQuery));
    }

}
