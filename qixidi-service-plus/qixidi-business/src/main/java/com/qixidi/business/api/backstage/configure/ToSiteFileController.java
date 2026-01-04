package com.qixidi.business.api.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToSiteFileBo;
import com.qixidi.business.domain.vo.configure.ToSiteFileVo;
import com.qixidi.business.service.configure.IToSiteFileService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
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
public class ToSiteFileController {

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
    public ToSiteFileVo getInfo(@PathVariable("id") Long id) {
        return iToSiteFileService.queryById(id);
    }

    /**
     * 新增网站文档
     */
    @SaCheckPermission("site:siteFile:add")
    @Log(title = "网站文档", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody ToSiteFileBo bo) {
        iToSiteFileService.insertByBo(bo);
    }

    /**
     * 修改网站文档
     */
    @SaCheckPermission("site:siteFile:edit")
    @Log(title = "网站文档", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ToSiteFileBo bo) {
        iToSiteFileService.updateByBo(bo);
    }

    /**
     * 删除网站文档
     */
    @SaCheckPermission("site:siteFile:remove")
    @Log(title = "网站文档", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iToSiteFileService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

