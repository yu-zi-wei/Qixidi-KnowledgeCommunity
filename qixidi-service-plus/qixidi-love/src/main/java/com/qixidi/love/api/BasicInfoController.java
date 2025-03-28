/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 15:01
 * @version 1.0
 */
package com.qixidi.love.api;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.qixidi.love.domain.bo.BasicInfoBo;
import com.qixidi.love.domain.vo.BasicInfoVo;
import com.qixidi.love.service.IBasicInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 【爱情小站】网站基本信息
 *
 * @author ziwei
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
    public R<BasicInfoVo> getInfo(@PathVariable Long id) {
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
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iBasicInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
