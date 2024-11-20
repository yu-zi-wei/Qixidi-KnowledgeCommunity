package com.aurora.web.controller.business.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.user.BrowsingHistoryBo;
import com.aurora.business.domain.vo.user.BrowsingHistoryVo;
import com.aurora.business.service.IBrowsingHistoryService;
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
 * 用户浏览历史Controller
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@Api(value = "用户浏览历史控制器", tags = {"用户浏览历史管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/browsing/history")
public class BrowsingHistoryController extends BaseController {

    private final IBrowsingHistoryService iBrowsingHistoryService;

    /**
     * 查询用户浏览历史列表
     */
    @ApiOperation("查询用户浏览历史列表")
    @SaCheckPermission("browsing:history:list")
    @GetMapping("/list")
    public TableDataInfo<BrowsingHistoryVo> list(@Validated(QueryGroup.class) BrowsingHistoryBo bo, PageQuery pageQuery) {
        return iBrowsingHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户浏览历史列表
     */
    @ApiOperation("导出用户浏览历史列表")
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
    @ApiOperation("获取用户浏览历史详细信息")
    @SaCheckPermission("browsing:history:query")
    @GetMapping("/{id}")
    public R<BrowsingHistoryVo> getInfo(@ApiParam("主键")
                                        @NotNull(message = "主键不能为空")
                                        @PathVariable("id") Long id) {
        return R.ok(iBrowsingHistoryService.queryById(id));
    }

    /**
     * 新增用户浏览历史
     */
    @ApiOperation("新增用户浏览历史")
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
    @ApiOperation("修改用户浏览历史")
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
    @ApiOperation("删除用户浏览历史")
    @SaCheckPermission("browsing:history:remove")
    @Log(title = "用户浏览历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iBrowsingHistoryService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
