package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.domain.bo.user.UserOrderBo;
import com.aurora.business.domain.vo.user.UserOrderVo;
import com.aurora.business.service.AliAayService;
import com.aurora.business.service.user.IUserOrderService;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.helper.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@Api(value = "用户订单控制器", tags = {"用户订单管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/order")
public class FdUserOrderController extends BaseController {
    private final IUserOrderService iUserOrderService;
    private final AliAayService aliAayService;

    /**
     * 查询用户订单列表
     */
    @ApiOperation("查询用户订单列表")
    @GetMapping("/list")
    public TableDataInfo<UserOrderVo> list(@Validated(QueryGroup.class) UserOrderBo bo, PageQuery pageQuery) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return iUserOrderService.queryPageList(bo, pageQuery);
    }


    /**
     * 获取用户订单详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("获取用户订单详细信息")
    @GetMapping("/info/{id}")
    public R<UserOrderVo> getInfo(@ApiParam("主键")
                                  @NotNull(message = "主键不能为空")
                                  @PathVariable("id") Long id) {
        return R.ok(iUserOrderService.queryById(id));
    }

    /**
     * 删除用户订单
     */
    @ApiOperation("删除用户订单")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteOrder(@ApiParam("主键")
                               @NotNull(message = "主键不能为空")
                               @PathVariable("id") Long id) {
        return toAjax(iUserOrderService.deleteOrder(id) ? 1 : 0);
    }

}
