package com.qixidi.system.api.article;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.business.domain.bo.article.ArticleAuditBo;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 文章信息管理
 *
 * @author aurora
 * @date 2022-08-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/article/information")
@LogModule("文章信息")
public class ArticleInformationController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 查询文章信息列表
     */
    @PostMapping("/list")
    public TableDataInfo<ArticleInformationVo> index(@Validated(QueryGroup.class) @RequestBody ArticleInformationBo bo) {
        return iArticleInformationService.index(bo, bo);
    }

    /**
     * 导出文章信息列表
     */
    @SaCheckPermission("article:information:export")
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ArticleInformationBo bo, HttpServletResponse response) {
        List<ArticleInformationVo> list = iArticleInformationService.queryList(bo);
        ExcelUtil.exportExcel(list, "文章信息", ArticleInformationVo.class, response);
    }

    /**
     * 获取文章信息详细信息
     */
    @SaCheckPermission("article:information:query")
    @GetMapping("/{id}")
    public ArticleInformationVo getInfo(@PathVariable("id") Long id) {
        return iArticleInformationService.queryById(id);
    }

    /**
     * 修改文章信息
     */
    @SaCheckPermission("article:information:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ArticleInformationBo bo) {
        iArticleInformationService.updateByBo(bo);
    }

    /**
     * 文章审核（后台终审：通过/拒绝）——独立于编辑，无需文章全字段
     */
    @SaCheckPermission("article:information:edit")
    @Log(title = "审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/audit")
    public void audit(@Validated @RequestBody ArticleAuditBo bo) {
        iArticleInformationService.audit(bo);
    }

    /**
     * 删除文章信息
     */
    @SaCheckPermission("article:information:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iArticleInformationService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

