package com.qixidi.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;

/**
 * 充值信息对象 to_recharge_info
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@TableName("to_recharge_info")
public class ToRechargeInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 充值金额
     */
    @ExcelProperty(value = "充值金额")
    private BigDecimal money;
    /**
     * 获得币的数量
     */
    @ExcelProperty(value = "获得币的数量")
    private Long currency;

    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 是否开启打折活动（0：未开启，1：活动中）
     */
    @ExcelDictFormat(readConverterExp = "0=：未开启，1：活动中")
    private Integer isDiscount;
    /**
     * 具体折扣
     */
    @ExcelProperty(value = "具体折扣")
    private Integer discount;
    /**
     * 打折活动开启时间
     */
    @ExcelProperty(value = "打折活动开启时间")
    private Date startTime;
    /**
     * 活动结束时间
     */
    @ExcelProperty(value = "活动结束时间")
    private Date endTime;
    /**
     * 会员折扣（0：无会员折扣，非零：具体折扣）
     */
    @ExcelDictFormat(readConverterExp = "0=：无会员折扣，非零：具体折扣")
    private Integer memberDiscount;

}

