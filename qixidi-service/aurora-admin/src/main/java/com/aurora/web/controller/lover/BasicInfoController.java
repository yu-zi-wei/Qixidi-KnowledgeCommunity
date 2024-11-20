/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 15:01
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
import com.aurora.lover.domain.bo.BasicInfoBo;
import com.aurora.lover.domain.vo.BasicInfoVo;
import com.aurora.lover.service.IBasicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 网站基本信息
 *
 * @author ruoyi
 * @date 2022-11-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/basic/info")
public class BasicInfoController extends BaseController {

    private final IBasicInfoService iBasicInfoService;

    /**
     * 查询网站基本信息列表
     */
    @SaCheckPermission("basic:info:list")
    @GetMapping("/list")
    public TableDataInfo<BasicInfoVo> list(BasicInfoBo bo, PageQuery pageQuery) {
        return iBasicInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出网站基本信息列表
     */
    @SaCheckPermission("basic:info:export")
    @Log(title = "网站基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(BasicInfoBo bo, HttpServletResponse response) {
        List<BasicInfoVo> list = iBasicInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "网站基本信息", BasicInfoVo.class, response);
    }

    /**
     * 获取网站基本信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("basic:info:query")
    @GetMapping("/{id}")
    public R<BasicInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iBasicInfoService.queryById(id));
    }

    /**
     * 新增网站基本信息
     */
    @SaCheckPermission("basic:info:add")
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BasicInfoBo bo) {
        return toAjax(iBasicInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改网站基本信息
     */
    @SaCheckPermission("basic:info:edit")
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody BasicInfoBo bo) {
        return toAjax(iBasicInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除网站基本信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("basic:info:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iBasicInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
