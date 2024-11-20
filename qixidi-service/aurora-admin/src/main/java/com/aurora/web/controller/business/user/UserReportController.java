package com.aurora.web.controller.business.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.user.UserReportBo;
import com.aurora.business.domain.vo.user.UserReportVo;
import com.aurora.business.service.user.IUserReportService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户签到Controller
 *
 * @author aurora
 * @date 2023-04-10
 */
@Validated
@Api(value = "用户签到控制器", tags = {"用户签到管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/user/report")
public class UserReportController extends BaseController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @ApiOperation("查询用户签到列表")
    @SaCheckPermission("business:user:report:list")
    @GetMapping("/list")
    public TableDataInfo<UserReportVo> list(@Validated(QueryGroup.class) UserReportBo bo, PageQuery pageQuery) {
        return iUserReportService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户签到列表
     */
    @ApiOperation("导出用户签到列表")
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
    @ApiOperation("获取用户签到详细信息")
    @SaCheckPermission("business:user:report:query")
    @GetMapping("/{id}")
    public R<UserReportVo> getInfo(@ApiParam("主键")
                                   @NotNull(message = "主键不能为空")
                                   @PathVariable("id") Long id) {
        return R.ok(iUserReportService.queryById(id));
    }

    /**
     * 新增用户签到
     */
    @ApiOperation("新增用户签到")
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
    @ApiOperation("修改用户签到")
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
    @ApiOperation("删除用户签到")
    @SaCheckPermission("business:user:report:remove")
    @Log(title = "用户签到", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserReportService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

