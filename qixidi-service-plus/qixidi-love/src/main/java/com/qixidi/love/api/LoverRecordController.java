/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 17:00
 * @version 1.0
 */
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
import com.qixidi.love.domain.bo.LoverRecordBo;
import com.qixidi.love.domain.vo.LoverRecordVo;
import com.qixidi.love.service.ILoverRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;


/**
 * 【爱情小站】爱情记录
 *
 * @author ziwei
 * @date 2022-11-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/record")
public class LoverRecordController extends BaseController {

    private final ILoverRecordService iLoverRecordService;

    /**
     * 查询爱情记录列表
     */
    @SaCheckPermission("lover:record:list")
    @GetMapping("/list")
    public TableDataInfo<LoverRecordVo> list(LoverRecordBo bo, PageQuery pageQuery) {
        return iLoverRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出爱情记录列表
     */
    @SaCheckPermission("lover:record:export")
    @Log(title = "爱情记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LoverRecordBo bo, HttpServletResponse response) {
        List<LoverRecordVo> list = iLoverRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "爱情记录", LoverRecordVo.class, response);
    }

    /**
     * 获取爱情记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:record:query")
    @GetMapping("/{id}")
    public R<LoverRecordVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iLoverRecordService.queryById(id));
    }

    /**
     * 新增爱情记录
     */
    @SaCheckPermission("lover:record:add")
    @Log(title = "爱情记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LoverRecordBo bo) {
        return toAjax(iLoverRecordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改爱情记录
     */
    @SaCheckPermission("lover:record:edit")
    @Log(title = "爱情记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LoverRecordBo bo) {
        return toAjax(iLoverRecordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除爱情记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:record:remove")
    @Log(title = "爱情记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLoverRecordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
