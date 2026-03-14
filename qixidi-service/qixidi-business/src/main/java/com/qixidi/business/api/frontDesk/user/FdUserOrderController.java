package com.qixidi.business.api.frontDesk.user;

import com.qixidi.business.domain.bo.user.UserOrderBo;
import com.qixidi.business.domain.vo.user.UserOrderVo;
import com.qixidi.business.service.user.IUserOrderService;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import com.qixidi.auth.helper.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】用户订单管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/order")
public class FdUserOrderController {
    private final IUserOrderService iUserOrderService;

    /**
     * 查询用户订单列表
     */
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
    @GetMapping("/info/{id}")
    public UserOrderVo getInfo(@PathVariable("id") Long id) {
        return iUserOrderService.queryById(id);
    }

    /**
     * 删除用户订单
     */
    @DeleteMapping("/delete/{id}")
    public Integer deleteOrder(@PathVariable("id") Long id) {
        return iUserOrderService.deleteOrder(id) ? 1 : 0;
    }

}
