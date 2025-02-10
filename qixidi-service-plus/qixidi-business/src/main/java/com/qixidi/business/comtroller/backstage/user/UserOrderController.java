package com.qixidi.business.comtroller.backstage.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.UserOrderBo;
import com.qixidi.business.domain.vo.user.UserOrderVo;
import com.qixidi.business.service.user.IUserOrderService;
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
    public R<UserOrderVo> getInfo(@PathVariable("id") Long id) {
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
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iUserOrderService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

