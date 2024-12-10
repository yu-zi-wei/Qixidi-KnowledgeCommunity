/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 16:02
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
import com.aurora.lover.domain.bo.RepertoireBo;
import com.aurora.lover.domain.vo.RepertoireVo;
import com.aurora.lover.service.IRepertoireService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 【爱情小站】爱情清单
 *
 * @author ziwei
 * @date 2022-11-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/repertoire")
public class RepertoireController extends BaseController {

    private final IRepertoireService iRepertoireService;

    /**
     * 查询爱情清单列表
     */
    @SaCheckPermission("lover:repertoire:list")
    @GetMapping("/list")
    public TableDataInfo<RepertoireVo> list(RepertoireBo bo, PageQuery pageQuery) {
        return iRepertoireService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出爱情清单列表
     */
    @SaCheckPermission("lover:repertoire:export")
    @Log(title = "爱情清单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RepertoireBo bo, HttpServletResponse response) {
        List<RepertoireVo> list = iRepertoireService.queryList(bo);
        ExcelUtil.exportExcel(list, "爱情清单", RepertoireVo.class, response);
    }

    /**
     * 获取爱情清单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:repertoire:query")
    @GetMapping("/{id}")
    public R<RepertoireVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable Long id) {
        return R.ok(iRepertoireService.queryById(id));
    }

    /**
     * 新增爱情清单
     */
    @SaCheckPermission("lover:repertoire:add")
    @Log(title = "爱情清单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RepertoireBo bo) {
        return toAjax(iRepertoireService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改爱情清单
     */
    @SaCheckPermission("lover:repertoire:edit")
    @Log(title = "爱情清单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RepertoireBo bo) {
        return toAjax(iRepertoireService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除爱情清单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:repertoire:remove")
    @Log(title = "爱情清单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iRepertoireService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
