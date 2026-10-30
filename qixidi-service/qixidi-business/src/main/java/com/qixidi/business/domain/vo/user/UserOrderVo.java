package com.qixidi.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import com.qixidi.business.domain.entity.user.UserOrder;


/**
 * 用户订单视图对象 b_user_order
 *
 * @author aurora
 * @date 2023-04-04
 */
@Data
@ExcelIgnoreUnannotated
public class UserOrderVo extends UserOrder {
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

