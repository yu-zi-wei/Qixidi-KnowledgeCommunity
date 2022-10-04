package com.aurora.web.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.business.domain.bo.user.TripartiteUserBo;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import com.aurora.system.service.ITripartiteUserService;
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
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-06-12
 */
@Validated
@Api(value = "【请填写功能名称】控制器", tags = {"【请填写功能名称】管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/tripartite/user")
public class TripartiteUserController extends BaseController {

    private final ITripartiteUserService iTripartiteUserService;

    /**
     * 查询【请填写功能名称】列表
     */
    @ApiOperation("查询【请填写功能名称】列表")
    @SaCheckPermission("system:main:list")
    @GetMapping("/list")
    public TableDataInfo<TripartiteUserVo> list(@Validated(QueryGroup.class) TripartiteUserBo bo, PageQuery pageQuery) {
        return iTripartiteUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @ApiOperation("导出【请填写功能名称】列表")
    @SaCheckPermission("system:main:export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TripartiteUserBo bo, HttpServletResponse response) {
        List<TripartiteUserVo> list = iTripartiteUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "【请填写功能名称】", TripartiteUserVo.class, response);
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @ApiOperation("获取【请填写功能名称】详细信息")
    @SaCheckPermission("system:main:query")
    @GetMapping("/{uuid}")
    public R<TripartiteUserVo> getInfo(@ApiParam("主键")
                                       @NotNull(message = "主键不能为空")
                                       @PathVariable("uuid") String uuid) {
        return R.ok(iTripartiteUserService.queryById(uuid));
    }

    /**
     * 新增【请填写功能名称】
     */
    @ApiOperation("新增【请填写功能名称】")
    @SaCheckPermission("system:main:add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TripartiteUserBo bo) {
        return toAjax(iTripartiteUserService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改【请填写功能名称】
     */
    @ApiOperation("修改【请填写功能名称】")
    @SaCheckPermission("system:main:edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TripartiteUserBo bo) {
        return toAjax(iTripartiteUserService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除【请填写功能名称】
     */
    @ApiOperation("删除【请填写功能名称】")
    @SaCheckPermission("system:main:remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uuids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable String[] uuids) {
        return toAjax(iTripartiteUserService.deleteWithValidByIds(Arrays.asList(uuids), true) ? 1 : 0);
    }

}
