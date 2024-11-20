package com.aurora.web.controller.business.privateUser;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.business.service.privateUser.IPrivateUserService;
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
 * 私信记录Controller
 *
 * @author aurora
 * @date 2023-03-23
 */
@Validated
@Api(value = "私信记录控制器", tags = {"私信记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/private/user")
public class PrivateUserController extends BaseController {

    private final IPrivateUserService iPrivateUserService;

    /**
     * 查询私信记录列表
     */
    @ApiOperation("查询私信记录列表")
    @GetMapping("/list")
    public TableDataInfo<PrivateUserVo> list(@Validated(QueryGroup.class) PrivateUserBo bo, PageQuery pageQuery) {
        return iPrivateUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出私信记录列表
     */
    @ApiOperation("导出私信记录列表")
    @Log(title = "私信记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated PrivateUserBo bo, HttpServletResponse response) {
        List<PrivateUserVo> list = iPrivateUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "私信记录", PrivateUserVo.class, response);
    }

    /**
     * 获取私信记录详细信息
     */
    @ApiOperation("获取私信记录详细信息")
    @GetMapping("/{id}")
    public R<PrivateUserVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("id") Long id) {
        return R.ok(iPrivateUserService.queryById(id));
    }

    /**
     * 新增私信记录
     */
    @ApiOperation("新增私信记录")
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrivateUserBo bo) {
        return toAjax(iPrivateUserService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改私信记录
     */
    @ApiOperation("修改私信记录")
    @Log(title = "私信记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PrivateUserBo bo) {
        return toAjax(iPrivateUserService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除私信记录
     */
    @ApiOperation("删除私信记录")
    @SaCheckPermission("private:user:remove")
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iPrivateUserService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

