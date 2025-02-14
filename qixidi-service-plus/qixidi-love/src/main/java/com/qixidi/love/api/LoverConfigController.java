package com.qixidi.love.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.qixidi.love.domain.bo.LoverConfigBo;
import com.qixidi.love.domain.vo.LoverConfigVo;
import com.qixidi.love.service.ILoverConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 【爱情小站】基本配置
 *
 * @author ziwei
 * @date 2022-12-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/config")
public class LoverConfigController extends BaseController {
    private final ILoverConfigService iLoverConfigService;

    /**
     * 查询基本配置列表
     */
    @SaCheckPermission("lover:config:list")
    @GetMapping("/list")
    public TableDataInfo<LoverConfigVo> list(LoverConfigBo bo, PageQuery pageQuery) {
        return iLoverConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出基本配置列表
     */
    @SaCheckPermission("lover:config:export")
    @Log(title = "基本配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LoverConfigBo bo, HttpServletResponse response) {
        List<LoverConfigVo> list = iLoverConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "基本配置", LoverConfigVo.class, response);
    }

    /**
     * 获取基本配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:config:query")
    @GetMapping("/{id}")
    public R<LoverConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iLoverConfigService.queryById(id));
    }

    /**
     * 新增基本配置
     */
    @SaCheckPermission("lover:config:add")
    @Log(title = "基本配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LoverConfigBo bo) {
        return toAjax(iLoverConfigService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改基本配置
     */
    @SaCheckPermission("lover:config:edit")
    @Log(title = "基本配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LoverConfigBo bo) {
        return toAjax(iLoverConfigService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除基本配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:config:remove")
    @Log(title = "基本配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLoverConfigService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
