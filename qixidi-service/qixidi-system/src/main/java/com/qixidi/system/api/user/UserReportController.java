package com.qixidi.system.api.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.UserReportBo;
import com.qixidi.business.domain.vo.user.UserReportVo;
import com.qixidi.business.service.user.IUserReportService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
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
@RequestMapping("/system/user/report")
public class UserReportController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @SaCheckPermission("user:report:list")
    @PostMapping("/list")
    public TableDataInfo<UserReportVo> list(@Validated(QueryGroup.class) @RequestBody UserReportBo bo) {
        return iUserReportService.queryPageList(bo, bo);
    }

    /**
     * 导出用户签到列表
     */
    @SaCheckPermission("user:report:export")
    @Log(title = "用户签到", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserReportBo bo, HttpServletResponse response) {
        List<UserReportVo> list = iUserReportService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户签到", UserReportVo.class, response);
    }

    /**
     * 获取用户签到详细信息
     */
    @SaCheckPermission("user:report:query")
    @GetMapping("/{id}")
    public UserReportVo getInfo(@PathVariable("id") Long id) {
        return iUserReportService.queryById(id);
    }

    /**
     * 新增用户签到
     */
    @SaCheckPermission("user:report:add")
    @Log(title = "用户签到", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody UserReportBo bo) throws Exception {
        iUserReportService.insertByBo(bo);
    }

    /**
     * 修改用户签到
     */
    @SaCheckPermission("user:report:edit")
    @Log(title = "用户签到", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody UserReportBo bo) {
        iUserReportService.updateByBo(bo);
    }

    /**
     * 删除用户签到
     */
    @SaCheckPermission("user:report:remove")
    @Log(title = "用户签到", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iUserReportService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

