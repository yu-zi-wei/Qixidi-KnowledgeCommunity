package com.aurora.web.controller.business.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.aurora.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.aurora.business.service.privateUser.IPrivateNewsInfoService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
