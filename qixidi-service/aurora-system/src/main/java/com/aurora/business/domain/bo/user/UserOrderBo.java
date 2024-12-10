package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单业务对象 b_user_order
 *
 * @author aurora
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserOrderBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 充值信息id
     */
    @NotNull(message = "充值信息id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long rechargeInfoId;

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal rechargeMoney;

    /**
     * 获得A币数量
     */
    @NotNull(message = "获得A币数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long rechargeCurrency;


    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 折扣（0：无折扣，非0：具体折扣）
     */
    private Long discount;

    /**
     * 支付方式
     */
    @NotBlank(message = "支付方式不能为空", groups = {AddGroup.class, EditGroup.class})
    private String paymentMethod;

    /**
     * 支付状态（1：待支付，2：已支付，3：已过期）
     */
    private Long paymentState;

    /**
     * 支付时间
     */
    private Date paymentTime;

}
