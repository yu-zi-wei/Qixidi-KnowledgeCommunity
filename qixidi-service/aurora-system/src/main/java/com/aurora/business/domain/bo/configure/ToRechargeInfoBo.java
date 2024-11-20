package com.aurora.business.domain.bo.configure;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
@ApiModel("充值信息业务对象")
public class ToRechargeInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额", required = true)
    @NotNull(message = "充值金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long money;

    /**
     * 获得币的数量
     */
    @ApiModelProperty(value = "获得币的数量", required = true)
    @NotNull(message = "获得币的数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal currency;

    /**
     * 是否开启打折活动（0：未开启，1：活动中）
     */
    @ApiModelProperty(value = "是否开启打折活动（0：未开启，1：活动中）", required = true)
    @NotNull(message = "是否开启打折活动（0：未开启，1：活动中）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long isDiscount;

    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 具体折扣
     */
    @ApiModelProperty(value = "具体折扣", required = true)
    private Long discount;

    /**
     * 打折活动开启时间
     */
    @ApiModelProperty(value = "打折活动开启时间", required = true)
    private Date startTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间", required = true)
    private Date endTime;

    /**
     * 会员折扣（0：无会员折扣，非零：具体折扣）
     */
    @ApiModelProperty(value = "会员折扣（0：无会员折扣，非零：具体折扣）", required = true)
    @NotNull(message = "会员折扣（0：无会员折扣，非零：具体折扣）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberDiscount;

}

