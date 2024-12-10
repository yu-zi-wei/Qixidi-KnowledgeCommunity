/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:33
 * @version 1.0
 */
package com.aurora.web.controller.lover;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import com.aurora.lover.domain.bo.LoverCommentBo;
import com.aurora.lover.domain.vo.LoverCommentVo;
import com.aurora.lover.service.ILoverCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 【爱情小站】爱情祝福语
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/comment")
public class LoverCommentController extends BaseController {

    private final ILoverCommentService iLoverCommentService;

    /**
     * 查询爱情祝福语列表
     */
    @SaCheckPermission("lover:comment:list")
    @GetMapping("/list")
    public TableDataInfo<LoverCommentVo> list(LoverCommentBo bo, PageQuery pageQuery) {
        return iLoverCommentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出爱情祝福语列表
     */
    @SaCheckPermission("lover:comment:export")
    @Log(title = "爱情祝福语", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(LoverCommentBo bo, HttpServletResponse response) {
        List<LoverCommentVo> list = iLoverCommentService.queryList(bo);
        ExcelUtil.exportExcel(list, "爱情祝福语", LoverCommentVo.class, response);
    }

    /**
     * 获取爱情祝福语详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:comment:query")
    @GetMapping("/{id}")
    public R<LoverCommentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iLoverCommentService.queryById(id));
    }

    /**
     * 新增爱情祝福语
     */
    @SaCheckPermission("lover:comment:add")
    @Log(title = "爱情祝福语", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LoverCommentBo bo) {
        return toAjax(iLoverCommentService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改爱情祝福语
     */
    @SaCheckPermission("lover:comment:edit")
    @Log(title = "爱情祝福语", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LoverCommentBo bo) {
        return toAjax(iLoverCommentService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除爱情祝福语
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:comment:remove")
    @Log(title = "爱情祝福语", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLoverCommentService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
