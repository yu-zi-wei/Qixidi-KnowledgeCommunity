package com.qixidi.business.comtroller.backstage.special;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.special.SpecialInformationBo;
import com.qixidi.business.domain.vo.special.SpecialInformationVo;
import com.qixidi.business.service.special.ISpecialInformationService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 专栏信息管理
 *
 * @author aurora
 * @date 2022-08-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/special/information")
public class SpecialInformationController extends BaseController {

    private final ISpecialInformationService iSpecialInformationService;

    /**
     * 查询专栏文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/article/list")
    public R getArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iSpecialInformationService.getArticleList(bo, pageQuery));
    }

    /**
     * 查询专栏信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<SpecialInformationVo> list(@Validated(QueryGroup.class) SpecialInformationBo bo, PageQuery pageQuery) {
        return iSpecialInformationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出专栏信息列表
     */
    @SaCheckPermission("special:information:export")
    @Log(title = "专栏信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated SpecialInformationBo bo, HttpServletResponse response) {
        List<SpecialInformationVo> list = iSpecialInformationService.queryList(bo);
        ExcelUtil.exportExcel(list, "专栏信息", SpecialInformationVo.class, response);
    }

    /**
     * 获取专栏信息详细信息
     */
    @SaCheckPermission("special:information:query")
    @GetMapping("/{id}")
    public R<SpecialInformationVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iSpecialInformationService.queryById(id));
    }

    /**
     * 新增专栏信息
     */
    @Log(title = "专栏信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SpecialInformationBo bo) {
        return toAjax(iSpecialInformationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改专栏信息
     */
    @Log(title = "专栏信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SpecialInformationBo bo) {
        return toAjax(iSpecialInformationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除专栏信息
     */
    @Log(title = "专栏信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iSpecialInformationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
