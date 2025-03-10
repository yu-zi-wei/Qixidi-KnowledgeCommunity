package com.qixidi.business.comtroller.frontDesk.article;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
