package com.qixidi.business.domain.bo.configure;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值信息业务对象 to_recharge_info
 *
 * @author aurora
 * @date 2023-04-04
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToRechargeInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 充值金额
     */
    @NotNull(message = "充值金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long money;

    /**
     * 获得币的数量
     */
    @NotNull(message = "获得币的数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal currency;

    /**
     * 是否开启打折活动（0：未开启，1：活动中）
     */
    @NotNull(message = "是否开启打折活动（0：未开启，1：活动中）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long isDiscount;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 具体折扣
     */
    private Long discount;

    /**
     * 打折活动开启时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 会员折扣（0：无会员折扣，非零：具体折扣）
     */
    @NotNull(message = "会员折扣（0：无会员折扣，非零：具体折扣）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberDiscount;

}

