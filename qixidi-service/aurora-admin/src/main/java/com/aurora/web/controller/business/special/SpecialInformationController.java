package com.aurora.web.controller.business.special;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.special.SpecialInformationBo;
import com.aurora.business.domain.vo.special.SpecialInformationVo;
import com.aurora.business.service.special.ISpecialInformationService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 专栏信息Controller
 *
 * @author aurora
 * @date 2022-08-19
 */
@Validated
@Api(value = "专栏信息控制器", tags = {"专栏信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/special/information")
public class SpecialInformationController extends BaseController {

    private final ISpecialInformationService iSpecialInformationService;


    @ApiOperation("查询专栏文章列表")
    @GetMapping("/article/list")
    public R getArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        return R.ok(iSpecialInformationService.getArticleList(bo, pageQuery));
    }

    /**
     * 查询专栏信息列表
     */
    @ApiOperation("查询专栏信息列表")
    @GetMapping("/list")
    public TableDataInfo<SpecialInformationVo> list(@Validated(QueryGroup.class) SpecialInformationBo bo, PageQuery pageQuery) {
        return iSpecialInformationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出专栏信息列表
     */
    @ApiOperation("导出专栏信息列表")
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
    @ApiOperation("获取专栏信息详细信息")
    @SaCheckPermission("special:information:query")
    @GetMapping("/{id}")
    public R<SpecialInformationVo> getInfo(@ApiParam("主键")
                                           @NotNull(message = "主键不能为空")
                                           @PathVariable("id") Long id) {
        return R.ok(iSpecialInformationService.queryById(id));
    }

    /**
     * 新增专栏信息
     */
    @ApiOperation("新增专栏信息")
    @Log(title = "专栏信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SpecialInformationBo bo) {
        return toAjax(iSpecialInformationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改专栏信息
     */
    @ApiOperation("修改专栏信息")
    @Log(title = "专栏信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SpecialInformationBo bo) {
        return toAjax(iSpecialInformationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除专栏信息
     */
    @ApiOperation("删除专栏信息")
    @Log(title = "专栏信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSpecialInformationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
