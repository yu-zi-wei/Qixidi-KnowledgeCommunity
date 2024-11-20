package com.aurora.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 用户签到对象 user_report
 *
 * @author aurora
 * @date 2023-04-10
 */
@Data
@TableName("user_report")
public class UserReport {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * uid
     */
    private String uid;
    /**
     * 签到时间
     */
    private Date reportTime;
    /**
     * 获得a币数量
     */
    private Long aCurrency;

}
