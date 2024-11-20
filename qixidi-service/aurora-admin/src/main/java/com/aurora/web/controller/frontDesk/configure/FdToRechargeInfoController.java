package com.aurora.web.controller.frontDesk.configure;

import com.aurora.business.domain.bo.configure.ToRechargeInfoBo;
import com.aurora.business.domain.vo.configure.ToRechargeInfoVo;
import com.aurora.business.service.configure.IToRechargeInfoService;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "充值信息控制器", tags = {"充值信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/rechargeInfo")
public class FdToRechargeInfoController {

    private final IToRechargeInfoService iToRechargeInfoService;

    /**
     * 查询充值信息列表
     */
    @ApiOperation("查询充值信息列表")
    @GetMapping("/list")
    public TableDataInfo<ToRechargeInfoVo> list(@Validated(QueryGroup.class) ToRechargeInfoBo bo) {
        return iToRechargeInfoService.list(bo);
    }
}
