package com.qixidi.business.api.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToToolInfoBo;
import com.qixidi.business.domain.vo.configure.ToToolInfoVo;
import com.qixidi.business.service.configure.IToToolInfoService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
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
 * 工具信息管理
 *
 * @author aurora
 * @date 2022-10-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/toolInfo")
public class ToToolInfoController {

    private final IToToolInfoService iToToolInfoService;

    /**
     * 查询工具信息列表
     */
    @SaCheckPermission("business:toolInfo:list")
    @GetMapping("/list")
    public Object list(@Validated(QueryGroup.class) ToToolInfoBo bo, PageQuery pageQuery) {
        return iToToolInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出工具信息列表
     */
    @SaCheckPermission("business:toolInfo:export")
    @Log(title = "工具信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToToolInfoBo bo, HttpServletResponse response) {
        List<ToToolInfoVo> list = iToToolInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "工具信息", ToToolInfoVo.class, response);
    }

    /**
     * 获取工具信息详细信息
     */
    @SaCheckPermission("business:toolInfo:query")
    @GetMapping("/{id}")
    public ToToolInfoVo getInfo(@PathVariable("id") Long id) {
        return iToToolInfoService.queryById(id);
    }

    /**
     * 新增工具信息
     */
    @SaCheckPermission("business:toolInfo:add")
    @Log(title = "工具信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody ToToolInfoBo bo) {
        iToToolInfoService.insertByBo(bo);
    }

    /**
     * 修改工具信息
     */
    @SaCheckPermission("business:toolInfo:edit")
    @Log(title = "工具信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ToToolInfoBo bo) {
        iToToolInfoService.updateByBo(bo);
    }

    /**
     * 删除工具信息
     */
    @SaCheckPermission("business:toolInfo:remove")
    @Log(title = "工具信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iToToolInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
