package com.qixidi.business.api.backstage.article;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

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
@RequestMapping("/business/article/information")
public class ArticleInformationController {

    private final IArticleInformationService iArticleInformationService;

    /**
     * 查询文章信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<ArticleInformationVo> index(@Validated(QueryGroup.class) ArticleInformationBo bo, PageQuery pageQuery) {
        return iArticleInformationService.index(bo, pageQuery);
    }

    /**
     * 导出文章信息列表
     */
    @SaCheckPermission("article:information:export")
    @Log(title = "文章信息", businessType = BusinessType.EXPORT)
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
    @Log(title = "文章信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ArticleInformationBo bo) {
        iArticleInformationService.updateByBo(bo);
    }

    /**
     * 删除文章信息
     */
    @SaCheckPermission("article:information:remove")
    @Log(title = "文章信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iArticleInformationService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

