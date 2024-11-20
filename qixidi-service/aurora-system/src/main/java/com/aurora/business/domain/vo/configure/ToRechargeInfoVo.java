package com.aurora.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 充值信息视图对象 to_recharge_info
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@ApiModel("充值信息视图对象")
@ExcelIgnoreUnannotated
public class ToRechargeInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 充值金额
     */
    @ExcelProperty(value = "充值金额")
    @ApiModelProperty("充值金额")
    private BigDecimal money;

    /**
     * 获得币的数量
     */
    @ExcelProperty(value = "获得币的数量")
    @ApiModelProperty("获得币的数量")
    private Long currency;

    /**
     * 是否开启打折活动（0：未开启，1：活动中）
     */
    @ExcelProperty(value = "是否开启打折活动", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：未开启，1：活动中")
    @ApiModelProperty("是否开启打折活动（0：未开启，1：活动中）")
    private Integer isDiscount;


    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 具体折扣
     */
    @ExcelProperty(value = "具体折扣")
    @ApiModelProperty("具体折扣")
    private Integer discount;

    /**
     * 打折活动开启时间
     */
    @ExcelProperty(value = "打折活动开启时间")
    @ApiModelProperty("打折活动开启时间")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @ExcelProperty(value = "活动结束时间")
    @ApiModelProperty("活动结束时间")
    private Date endTime;

    /**
     * 会员折扣（0：无会员折扣，非零：具体折扣）
     */
    @ExcelProperty(value = "会员折扣", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：无会员折扣，非零：具体折扣")
    @ApiModelProperty("会员折扣（0：无会员折扣，非零：具体折扣）")
    private Integer memberDiscount;


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
