/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:23
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
import com.aurora.lover.domain.bo.AboutBo;
import com.aurora.lover.domain.vo.AboutVo;
import com.aurora.lover.service.IAboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;


/**
 * 【爱情小站】关于我们
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/about")
public class AboutController extends BaseController {

    private final IAboutService iAboutService;

    /**
     * 查询关于我们列表
     */
    @SaCheckPermission("lover:about:list")
    @GetMapping("/list")
    public TableDataInfo<AboutVo> list(AboutBo bo, PageQuery pageQuery) {
        return iAboutService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出关于我们列表
     */
    @SaCheckPermission("lover:about:export")
    @Log(title = "关于我们", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AboutBo bo, HttpServletResponse response) {
        List<AboutVo> list = iAboutService.queryList(bo);
        ExcelUtil.exportExcel(list, "关于我们", AboutVo.class, response);
    }

    /**
     * 获取关于我们详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:about:query")
    @GetMapping("/{id}")
    public R<AboutVo> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long id) {
        return R.ok(iAboutService.queryById(id));
    }

    /**
     * 新增关于我们
     */
    @SaCheckPermission("lover:about:add")
    @Log(title = "关于我们", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AboutBo bo) {
        return toAjax(iAboutService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改关于我们
     */
    @SaCheckPermission("lover:about:edit")
    @Log(title = "关于我们", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AboutBo bo) {
        return toAjax(iAboutService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除关于我们
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:about:remove")
    @Log(title = "关于我们", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAboutService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
