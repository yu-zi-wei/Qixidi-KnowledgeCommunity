package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;

/**
 * 用户订单对象 b_user_order
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@TableName("b_user_order")
@Accessors(chain = true)
public class UserOrder {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;
    /**
     * 充值信息id
     */
    @ExcelProperty(value = "充值信息id")
    private Long rechargeInfoId;
    /**
     * 充值金额
     */
    @ExcelProperty(value = "充值金额")
    private BigDecimal rechargeMoney;
    /**
     * 获得A币数量
     */
    @ExcelProperty(value = "获得A币数量")
    private Long rechargeCurrency;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 折扣（0：无折扣，非0：具体折扣）
     */
    @TableField("`discount`")
    @ExcelProperty(value = "折扣", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：无折扣，非0：具体折扣")
    private Integer discount;
    /**
     * 支付方式
     */
    @TableField("`payment_method`")
    @ExcelProperty(value = "支付方式")
    private String paymentMethod;
    /**
     * 支付状态（1：待支付，2：已支付，3：已过期）
     */
    @TableField("`payment_state`")
    @ExcelProperty(value = "支付状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：待支付，2：已支付，3：已过期")
    private Integer paymentState;
    /**
     * 支付时间
     */
    @ExcelProperty(value = "支付时间")
    private Date paymentTime;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}

