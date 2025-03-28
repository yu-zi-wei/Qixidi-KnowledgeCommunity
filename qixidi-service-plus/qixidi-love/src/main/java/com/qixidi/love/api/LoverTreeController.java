/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 17:00
 * @version 1.0
 */
package com.qixidi.love.api;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.love.domain.bo.LoverTreeBo;
import com.qixidi.love.domain.vo.LoverTreeVo;
import com.qixidi.love.service.ILoverTreeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 【爱情小站】爱情树
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/tree")
public class LoverTreeController extends BaseController {

    private final ILoverTreeService iLoverTreeService;

    /**
     * 查询爱情树列表
     */
    @SaCheckPermission("lover:tree:list")
    @GetMapping("/list")
    public TableDataInfo<LoverTreeVo> list(LoverTreeBo bo, PageQuery pageQuery) {
        return iLoverTreeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出爱情树列表
     */
    @SaCheckPermission("lover:tree:export")
    @Log(title = "爱情树", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LoverTreeBo bo, HttpServletResponse response) {
        List<LoverTreeVo> list = iLoverTreeService.queryList(bo);
        ExcelUtil.exportExcel(list, "爱情树", LoverTreeVo.class, response);
    }

    /**
     * 获取爱情树详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:tree:query")
    @GetMapping("/{id}")
    public R<LoverTreeVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iLoverTreeService.queryById(id));
    }

    /**
     * 新增爱情树
     */
    @SaCheckPermission("lover:tree:add")
    @Log(title = "爱情树", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LoverTreeBo bo) {
        return toAjax(iLoverTreeService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改爱情树
     */
    @SaCheckPermission("lover:tree:edit")
    @Log(title = "爱情树", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LoverTreeBo bo) {
        return toAjax(iLoverTreeService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除爱情树
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:tree:remove")
    @Log(title = "爱情树", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLoverTreeService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
