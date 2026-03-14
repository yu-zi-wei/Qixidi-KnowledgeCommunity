package com.qixidi.business.api.frontDesk.article;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【前台】文章信息接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserArticleController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 新增文章信息
     */
    @Log(title = "文章信息（新增文章信息）", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/insert/article")
    public ArticleInformationVo add(@Validated(AddGroup.class) @RequestBody ArticleInformationBo bo) {
        bo.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW.getCode());
        if (bo.getId() != null) {
            ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
            if (vo.getId() < -10) return vo;
            if (vo.getId() <= 0) throw new ServiceException("文章创建失败");
            return vo;
        }
        ArticleInformationVo vo = iArticleInformationService.insertByBo(bo);
        return vo;
    }

    /**
     * 更新文章信息
     */
    @Log(title = "文章信息（更新文章信息）", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/article")
    public ArticleInformationVo edit(@Validated(EditGroup.class) @RequestBody ArticleInformationBo bo) {
        ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
        return vo;
    }

    /**
     * 保存草稿
     */
    @Log(title = "文章信息（保存草稿）", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/save/draft")
    public ArticleInformationVo saveDraft(@Validated(EditGroup.class) @RequestBody ArticleInformationTwoBo bo) {
        ArticleInformationVo vo = iArticleInformationService.saveDraft(bo);
        return vo;
    }

    /**
     * 查询用户文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/list")
    public TableDataInfo<ArticleInformationVo> getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) throw new ServiceException("登录已过期");
        bo.setUserId(uuid);
        return TableDataInfo.build(iArticleInformationService.getArticleInfo(bo, pageQuery));
    }

    /**
     * 查询用户文章标题列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/title/list")
    public List<ArticleInformationVo> getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) throw new ServiceException("登录已过期");
        bo.setUserId(uuid);
        return iArticleInformationService.getArticleInfoList(bo, pageQuery);
    }

    /**
     * 文章编辑详情
     *
     * @param id
     * @return
     */
    @GetMapping("/get/article/{id}")
    public ArticleInformationVo getArticle(@NotNull(message = "id不能为空") @PathVariable("id") String id) {
        return iArticleInformationService.getArticle(Long.valueOf(id));
    }

    /**
     * 删除用户文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/article/{id}")
    public void delete(@NotNull(message = "文章id不能为空") @PathVariable("id") Long id) {
        iArticleInformationService.delete(id);
    }

    /**
     * 获取用户最近文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/lately/article/list")
    public TableDataInfo<ArticleInformationVo> latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) throw new ServiceException("登录已过期");
        bo.setUserId(uuid);
        return iArticleInformationService.latelyArticleList(bo, pageQuery);
    }

}
