package com.aurora.web.controller.business.article;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
public class ArticleInformationController extends BaseController {

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
    public R<ArticleInformationVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iArticleInformationService.queryById(id));
    }

    /**
     * 修改文章信息
     */
    @SaCheckPermission("article:information:edit")
    @Log(title = "文章信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<ArticleInformationVo> edit(@Validated(EditGroup.class) @RequestBody ArticleInformationBo bo) {
        ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
        return vo.getId() > 0 ? R.ok(vo) : R.fail();
    }

    /**
     * 删除文章信息
     */
    @SaCheckPermission("article:information:remove")
    @Log(title = "文章信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iArticleInformationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

