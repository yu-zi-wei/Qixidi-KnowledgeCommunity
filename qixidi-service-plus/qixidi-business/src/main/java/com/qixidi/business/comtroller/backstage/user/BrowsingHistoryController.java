package com.qixidi.business.comtroller.backstage.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.BrowsingHistoryBo;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.qixidi.business.service.IBrowsingHistoryService;
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
 * 用户浏览历史管理
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/browsing/history")
public class BrowsingHistoryController extends BaseController {

    private final IBrowsingHistoryService iBrowsingHistoryService;

    /**
     * 查询用户浏览历史列表
     */
    @SaCheckPermission("browsing:history:list")
    @GetMapping("/list")
    public TableDataInfo<BrowsingHistoryVo> list(@Validated(QueryGroup.class) BrowsingHistoryBo bo, PageQuery pageQuery) {
        return iBrowsingHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户浏览历史列表
     */
    @SaCheckPermission("browsing:history:export")
    @Log(title = "用户浏览历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated BrowsingHistoryBo bo, HttpServletResponse response) {
        List<BrowsingHistoryVo> list = iBrowsingHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户浏览历史", BrowsingHistoryVo.class, response);
    }

    /**
     * 获取用户浏览历史详细信息
     */
    @SaCheckPermission("browsing:history:query")
    @GetMapping("/{id}")
    public R<BrowsingHistoryVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iBrowsingHistoryService.queryById(id));
    }

    /**
     * 新增用户浏览历史
     */
    @SaCheckPermission("browsing:history:add")
    @Log(title = "用户浏览历史", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BrowsingHistoryBo bo) {
        return toAjax(iBrowsingHistoryService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户浏览历史
     */
    @SaCheckPermission("browsing:history:edit")
    @Log(title = "用户浏览历史", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BrowsingHistoryBo bo) {
        return toAjax(iBrowsingHistoryService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户浏览历史
     */
    @SaCheckPermission("browsing:history:remove")
    @Log(title = "用户浏览历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iBrowsingHistoryService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
