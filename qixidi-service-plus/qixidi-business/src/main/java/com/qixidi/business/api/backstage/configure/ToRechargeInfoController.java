package com.qixidi.business.api.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToRechargeInfoBo;
import com.qixidi.business.domain.vo.configure.ToRechargeInfoVo;
import com.qixidi.business.service.configure.IToRechargeInfoService;
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
 * 充值信息管理
 *
 * @author aurora
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/rechargeInfo")
public class ToRechargeInfoController extends BaseController {

    private final IToRechargeInfoService iToRechargeInfoService;

    /**
     * 查询充值信息列表
     */
    @SaCheckPermission("business:rechargeInfo:list")
    @GetMapping("/list")
    public TableDataInfo<ToRechargeInfoVo> list(@Validated(QueryGroup.class) ToRechargeInfoBo bo, PageQuery pageQuery) {
        return iToRechargeInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出充值信息列表
     */
    @SaCheckPermission("business:rechargeInfo:export")
    @Log(title = "充值信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToRechargeInfoBo bo, HttpServletResponse response) {
        List<ToRechargeInfoVo> list = iToRechargeInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "充值信息", ToRechargeInfoVo.class, response);
    }

    /**
     * 获取充值信息详细信息
     */
    @SaCheckPermission("business:rechargeInfo:query")
    @GetMapping("/{id}")
    public R<ToRechargeInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iToRechargeInfoService.queryById(id));
    }

    /**
     * 新增充值信息
     */
    @SaCheckPermission("business:rechargeInfo:add")
    @Log(title = "充值信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToRechargeInfoBo bo) {
        return toAjax(iToRechargeInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改充值信息
     */
    @SaCheckPermission("business:rechargeInfo:edit")
    @Log(title = "充值信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToRechargeInfoBo bo) {
        return toAjax(iToRechargeInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除充值信息
     */
    @SaCheckPermission("business:rechargeInfo:remove")
    @Log(title = "充值信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iToRechargeInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

