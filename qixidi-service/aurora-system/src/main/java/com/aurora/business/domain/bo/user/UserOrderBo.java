package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单业务对象 user_order
 *
 * @author aurora
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户订单业务对象")
public class UserOrderBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    private String nickname;

    /**
     * 充值信息id
     */
    @ApiModelProperty(value = "充值信息id", required = true)
    @NotNull(message = "充值信息id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long rechargeInfoId;

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额", required = true)
    @NotNull(message = "充值金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal rechargeMoney;

    /**
     * 获得A币数量
     */
    @ApiModelProperty(value = "获得A币数量", required = true)
    @NotNull(message = "获得A币数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long rechargeCurrency;


    /**
     * 商品名称
     */
    private String commodityName;

    /**
     * 折扣（0：无折扣，非0：具体折扣）
     */
    @ApiModelProperty(value = "折扣（0：无折扣，非0：具体折扣）", required = true)
    private Long discount;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式", required = true)
    @NotBlank(message = "支付方式不能为空", groups = {AddGroup.class, EditGroup.class})
    private String paymentMethod;

    /**
     * 支付状态（1：待支付，2：已支付，3：已过期）
     */
    @ApiModelProperty(value = "支付状态（1：待支付，2：已支付，3：已过期）", required = true)
    private Long paymentState;

    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间", required = true)
    private Date paymentTime;

}
