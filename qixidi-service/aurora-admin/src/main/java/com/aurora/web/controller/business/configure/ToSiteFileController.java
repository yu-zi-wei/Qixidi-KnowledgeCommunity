package com.aurora.web.controller.business.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.configure.ToSiteFileBo;
import com.aurora.business.domain.vo.configure.ToSiteFileVo;
import com.aurora.business.service.configure.IToSiteFileService;
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
 * 网站文档Controller
 *
 * @author aurora
 * @date 2022-10-21
 */
@Validated
@Api(value = "网站文档控制器", tags = {"网站文档管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/site/file")
public class ToSiteFileController extends BaseController {

    private final IToSiteFileService iToSiteFileService;

    /**
     * 查询网站文档列表
     */
    @ApiOperation("查询网站文档列表")
    @SaCheckPermission("site:siteFile:list")
    @GetMapping("/list")
    public TableDataInfo<ToSiteFileVo> list(@Validated(QueryGroup.class) ToSiteFileBo bo, PageQuery pageQuery) {
        return iToSiteFileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出网站文档列表
     */
    @ApiOperation("导出网站文档列表")
    @SaCheckPermission("site:siteFile:export")
    @Log(title = "网站文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToSiteFileBo bo, HttpServletResponse response) {
        List<ToSiteFileVo> list = iToSiteFileService.queryList(bo);
        ExcelUtil.exportExcel(list, "网站文档", ToSiteFileVo.class, response);
    }

    /**
     * 获取网站文档详细信息
     */
    @ApiOperation("获取网站文档详细信息")
    @SaCheckPermission("site:siteFile:query")
    @GetMapping("/{id}")
    public R<ToSiteFileVo> getInfo(@ApiParam("主键")
                                   @NotNull(message = "主键不能为空")
                                   @PathVariable("id") Long id) {
        return R.ok(iToSiteFileService.queryById(id));
    }

    /**
     * 新增网站文档
     */
    @ApiOperation("新增网站文档")
    @SaCheckPermission("site:siteFile:add")
    @Log(title = "网站文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToSiteFileBo bo) {
        return toAjax(iToSiteFileService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改网站文档
     */
    @ApiOperation("修改网站文档")
    @SaCheckPermission("site:siteFile:edit")
    @Log(title = "网站文档", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToSiteFileBo bo) {
        return toAjax(iToSiteFileService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除网站文档
     */
    @ApiOperation("删除网站文档")
    @SaCheckPermission("site:siteFile:remove")
    @Log(title = "网站文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iToSiteFileService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

