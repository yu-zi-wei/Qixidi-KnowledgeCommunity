package com.qixidi.business.comtroller.backstage.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.UserReportBo;
import com.qixidi.business.domain.vo.user.UserReportVo;
import com.qixidi.business.service.user.IUserReportService;
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
 * 用户签到管理
 *
 * @author aurora
 * @date 2023-04-10
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/user/report")
public class UserReportController extends BaseController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @SaCheckPermission("business:user:report:list")
    @GetMapping("/list")
    public TableDataInfo<UserReportVo> list(@Validated(QueryGroup.class) UserReportBo bo, PageQuery pageQuery) {
        return iUserReportService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户签到列表
     */
    @SaCheckPermission("business:user:report:export")
    @Log(title = "用户签到", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserReportBo bo, HttpServletResponse response) {
        List<UserReportVo> list = iUserReportService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户签到", UserReportVo.class, response);
    }

    /**
     * 获取用户签到详细信息
     */
    @SaCheckPermission("business:user:report:query")
    @GetMapping("/{id}")
    public R<UserReportVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iUserReportService.queryById(id));
    }

    /**
     * 新增用户签到
     */
    @SaCheckPermission("business:user:report:add")
    @Log(title = "用户签到", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserReportBo bo) throws Exception {
        return toAjax(iUserReportService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户签到
     */
    @SaCheckPermission("business:user:report:edit")
    @Log(title = "用户签到", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserReportBo bo) {
        return toAjax(iUserReportService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户签到
     */
    @SaCheckPermission("business:user:report:remove")
    @Log(title = "用户签到", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iUserReportService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

