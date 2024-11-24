package com.aurora.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
    private Long id;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 充值信息id
     */
    private Long rechargeInfoId;
    /**
     * 充值金额
     */
    private BigDecimal rechargeMoney;
    /**
     * 获得A币数量
     */
    private Long rechargeCurrency;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 折扣（0：无折扣，非0：具体折扣）
     */
    @TableField("`discount`")
    private Integer discount;
    /**
     * 支付方式
     */
    @TableField("`payment_method`")
    private String paymentMethod;
    /**
     * 支付状态（1：待支付，2：已支付，3：已过期）
     */
    @TableField("`payment_state`")
    private Integer paymentState;
    /**
     * 支付时间
     */
    private Date paymentTime;
    /**
     * 创建时间
     */
    private Date createTime;

}

