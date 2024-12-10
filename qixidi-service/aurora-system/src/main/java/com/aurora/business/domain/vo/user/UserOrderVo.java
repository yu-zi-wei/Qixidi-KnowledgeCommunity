package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 用户订单视图对象 b_user_order
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class UserOrderVo {
    /**
     * id
     */
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
    @ExcelProperty(value = "折扣", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：无折扣，非0：具体折扣")
    private Integer discount;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String paymentMethod;

    /**
     * 支付状态（1：待支付，2：已支付，3：已过期）
     */
    @ExcelProperty(value = "支付状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：待支付，2：已支付，3：已过期")
    private Integer paymentState;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 支付时间
     */
    @ExcelProperty(value = "支付时间")
    private Date paymentTime;

    /**
     * 名称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户职业
     */
    private String occupation;


}

