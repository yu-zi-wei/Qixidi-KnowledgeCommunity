package com.qixidi.business.comtroller.frontDesk.configure;

import com.qixidi.business.domain.bo.configure.ToRechargeInfoBo;
import com.qixidi.business.domain.vo.configure.ToRechargeInfoVo;
import com.qixidi.business.service.configure.IToRechargeInfoService;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【前台】充值信息管理
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/rechargeInfo")
public class FdToRechargeInfoController {

    private final IToRechargeInfoService iToRechargeInfoService;

    /**
     * 查询充值信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<ToRechargeInfoVo> list(@Validated(QueryGroup.class) ToRechargeInfoBo bo) {
        return iToRechargeInfoService.list(bo);
    }
}
