package com.aurora.web.controller.business.shield;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import com.aurora.business.domain.vo.shield.ToShieldWordVo;
import com.aurora.business.domain.bo.shield.ToShieldWordBo;
import com.aurora.business.service.shield.IToShieldWordService;
import com.aurora.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

/**
 * 屏蔽词库Controller
 *
 * @author aurora
 * @date 2023-02-20
 */
@Slf4j
@Validated
@Api(value = "屏蔽池库控制器", tags = {"屏蔽池库管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/shield/shieldWord")
public class ToShieldWordController extends BaseController {
    private final IToShieldWordService iToShieldWordService;

    /**
     * 查询屏蔽池库列表
     */
    @ApiOperation("查询屏蔽池库列表")
    @SaCheckPermission("shield:shieldWord:list")
    @GetMapping("/list")
    public TableDataInfo<ToShieldWordVo> list(@Validated(QueryGroup.class) ToShieldWordBo bo, PageQuery pageQuery) {

        return iToShieldWordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出屏蔽词库列表
     */
    @ApiOperation("导出屏蔽池库列表")
    @SaCheckPermission("shield:shieldWord:export")
    @Log(title = "屏蔽池库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToShieldWordBo bo, HttpServletResponse response) {
        List<ToShieldWordVo> list = iToShieldWordService.queryList(bo);
        ExcelUtil.exportExcel(list, "屏蔽词库", ToShieldWordVo.class, response);
    }

    /**
     * 获取屏蔽词库详细信息
     */
    @ApiOperation("获取屏蔽池库详细信息")
    @SaCheckPermission("shield:shieldWord:query")
    @GetMapping("/{id}")
    public R<ToShieldWordVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iToShieldWordService.queryById(id));
    }

    /**
     * 新增屏蔽词库
     */
    @ApiOperation("新增屏蔽词库")
    @SaCheckPermission("shield:shieldWord:add")
    @Log(title = "屏蔽池库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToShieldWordBo bo) {
        return toAjax(iToShieldWordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改屏蔽池库
     */
    @ApiOperation("修改屏蔽池库")
    @SaCheckPermission("shield:shieldWord:edit")
    @Log(title = "屏蔽池库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToShieldWordBo bo) {
        return toAjax(iToShieldWordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除屏蔽词库
     */
    @ApiOperation("删除屏蔽词库")
    @SaCheckPermission("shield:shieldWord:remove")
    @Log(title = "屏蔽池库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iToShieldWordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
