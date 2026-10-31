package com.qixidi.system.api.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.UserOrderBo;
import com.qixidi.business.domain.vo.user.UserOrderVo;
import com.qixidi.business.service.user.IUserOrderService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
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
 * 用户订单管理
 *
 * @author aurora
 * @date 2023-04-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/order")
@LogModule("用户订单")
public class UserOrderController {

    private final IUserOrderService iUserOrderService;

    /**
     * 查询用户订单列表
     */
    @SaCheckPermission("order:list")
    @PostMapping("/list")
    public TableDataInfo<UserOrderVo> list(@Validated(QueryGroup.class) @RequestBody UserOrderBo bo) {
        return iUserOrderService.queryPageList(bo, bo);
    }

    /**
     * 导出用户订单列表
     */
    @SaCheckPermission("order:export")
    @Log(title = "导出用户订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated UserOrderBo bo, HttpServletResponse response) {
        List<UserOrderVo> list = iUserOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户订单", UserOrderVo.class, response);
    }

    /**
     * 获取用户订单详细信息
     */
    @SaCheckPermission("order:query")
    @GetMapping("/{id}")
    public UserOrderVo getInfo(@PathVariable("id") Long id) {
        return iUserOrderService.queryById(id);
    }

    /**
     * 新增用户订单
     */
    @SaCheckPermission("order:add")
    @Log(title = "新增用户订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody UserOrderBo bo) {
        iUserOrderService.insertByBo(bo);
    }

    /**
     * 修改用户订单
     */
    @SaCheckPermission("order:edit")
    @Log(title = "修改用户订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody UserOrderBo bo) {
        iUserOrderService.updateByBo(bo);
    }

    /**
     * 删除用户订单
     */
    @SaCheckPermission("order:remove")
    @Log(title = "删除用户订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iUserOrderService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

