package com.aurora.web.controller.business.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.user.UserOrderBo;
import com.aurora.business.domain.vo.user.UserOrderVo;
import com.aurora.business.service.user.IUserOrderService;
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
 * 用户订单管理
 *
 * @author aurora
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/order")
public class UserOrderController extends BaseController {

    private final IUserOrderService iUserOrderService;

    /**
     * 查询用户订单列表
     */
    @SaCheckPermission("business:order:list")
    @GetMapping("/list")
    public TableDataInfo<UserOrderVo> list(@Validated(QueryGroup.class) UserOrderBo bo, PageQuery pageQuery) {
        return iUserOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户订单列表
     */
    @SaCheckPermission("business:order:export")
    @Log(title = "用户订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserOrderBo bo, HttpServletResponse response) {
        List<UserOrderVo> list = iUserOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户订单", UserOrderVo.class, response);
    }

    /**
     * 获取用户订单详细信息
     */
    @SaCheckPermission("business:order:query")
    @GetMapping("/{id}")
    public R<UserOrderVo> getInfo(@ApiParam("主键")
                                  @NotNull(message = "主键不能为空")
                                  @PathVariable("id") Long id) {
        return R.ok(iUserOrderService.queryById(id));
    }

    /**
     * 新增用户订单
     */
    @SaCheckPermission("business:order:add")
    @Log(title = "用户订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserOrderBo bo) {
        return toAjax(iUserOrderService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户订单
     */
    @SaCheckPermission("system:order:edit")
    @Log(title = "用户订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserOrderBo bo) {
        return toAjax(iUserOrderService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户订单
     */
    @SaCheckPermission("business:order:remove")
    @Log(title = "用户订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserOrderService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

