package com.qixidi.business.comtroller.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToSiteFileBo;
import com.qixidi.business.domain.vo.configure.ToSiteFileVo;
import com.qixidi.business.service.configure.IToSiteFileService;
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
 * 网站文档管理
 *
 * @author aurora
 * @date 2022-10-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/site/file")
public class ToSiteFileController extends BaseController {

    private final IToSiteFileService iToSiteFileService;

    /**
     * 查询网站文档列表
     */
    @SaCheckPermission("site:siteFile:list")
    @GetMapping("/list")
    public TableDataInfo<ToSiteFileVo> list(@Validated(QueryGroup.class) ToSiteFileBo bo, PageQuery pageQuery) {
        return iToSiteFileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出网站文档列表
     */
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
    @SaCheckPermission("site:siteFile:query")
    @GetMapping("/{id}")
    public R<ToSiteFileVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iToSiteFileService.queryById(id));
    }

    /**
     * 新增网站文档
     */
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
    @SaCheckPermission("site:siteFile:remove")
    @Log(title = "网站文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iToSiteFileService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

