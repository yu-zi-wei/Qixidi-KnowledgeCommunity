package com.qixidi.business.comtroller.backstage.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.qixidi.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.qixidi.business.service.privateUser.IPrivateNewsInfoService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
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
 * 私信记录管理
 *
 * @author aurora
 * @date 2023-03-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/private/newsInfo")
public class PrivateNewsInfoController extends BaseController {

    private final IPrivateNewsInfoService iPrivateNewsInfoService;

    /**
     * 查询私信记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<PrivateNewsInfoVo> list(@Validated(QueryGroup.class) PrivateNewsInfoBo bo, PageQuery pageQuery) {
        return iPrivateNewsInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出私信记录列表
     */
    @Log(title = "私信记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated PrivateNewsInfoBo bo, HttpServletResponse response) {
        List<PrivateNewsInfoVo> list = iPrivateNewsInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "私信记录", PrivateNewsInfoVo.class, response);
    }

    /**
     * 获取私信记录详细信息
     */
    @GetMapping("/{id}")
    public R<PrivateNewsInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iPrivateNewsInfoService.queryById(id));
    }

    /**
     * 新增私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrivateNewsInfoBo bo) {
        return toAjax(iPrivateNewsInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PrivateNewsInfoBo bo) {
        return toAjax(iPrivateNewsInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iPrivateNewsInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
