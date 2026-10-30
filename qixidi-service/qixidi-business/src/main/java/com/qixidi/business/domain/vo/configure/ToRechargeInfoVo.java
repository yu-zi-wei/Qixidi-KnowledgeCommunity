package com.qixidi.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import com.qixidi.business.domain.entity.configure.ToRechargeInfo;


/**
 * 充值信息视图对象 to_recharge_info
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class ToRechargeInfoVo extends ToRechargeInfo {

    private static final long serialVersionUID = 1L;



    /**
     * 活动是否结束
     */
    private Boolean isFinish = true;

    /**
     * 活动打折金额
     */
    private BigDecimal moveMoney;


    /**
     * 会员打折金额
     */
    private BigDecimal memberMoney;
    /**
     * 最终价格
     */
    private BigDecimal finalMoney;


}
