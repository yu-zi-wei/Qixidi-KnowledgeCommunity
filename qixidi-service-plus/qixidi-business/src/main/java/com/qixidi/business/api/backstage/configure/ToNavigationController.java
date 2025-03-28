package com.qixidi.business.api.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
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
 * 导航栏配置管理
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/configure/navigation")
public class ToNavigationController extends BaseController {

    private final IToNavigationService iToNavigationService;

    /**
     * 查询导航栏配置列表
     */
    @GetMapping("/list")
    public TableDataInfo<ToNavigationVo> list(@Validated(QueryGroup.class) ToNavigationBo bo, PageQuery pageQuery) {
        return iToNavigationService.queryPageListAdmin(bo, pageQuery);
    }

    /**
     * 导出导航栏配置列表
     */
    @SaCheckPermission("business:navigation:export")
    @Log(title = "导航栏配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToNavigationBo bo, HttpServletResponse response) {
        List<ToNavigationVo> list = iToNavigationService.queryList(bo);
        ExcelUtil.exportExcel(list, "导航栏配置", ToNavigationVo.class, response);
    }

    /**
     * 获取导航栏配置详细信息
     */
    @SaCheckPermission("business:navigation:query")
    @GetMapping("/{id}")
    public R<ToNavigationVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iToNavigationService.queryById(id));
    }

    /**
     * 新增导航栏配置
     */
    @SaCheckPermission("business:navigation:add")
    @Log(title = "导航栏配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToNavigationBo bo) {
        return toAjax(iToNavigationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改导航栏配置
     */
    @SaCheckPermission("business:navigation:edit")
    @Log(title = "导航栏配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToNavigationBo bo) {
        return toAjax(iToNavigationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除导航栏配置
     */
    @SaCheckPermission("business:navigation:remove")
    @Log(title = "导航栏配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iToNavigationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

