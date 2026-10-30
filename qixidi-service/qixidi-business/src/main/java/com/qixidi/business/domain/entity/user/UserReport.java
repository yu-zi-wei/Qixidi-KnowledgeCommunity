package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 用户签到对象 b_user_report
 *
 * @author aurora
 * @date 2023-04-10
 */
@Data
@TableName("b_user_report")
public class UserReport {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * uid
     */
    @ExcelProperty(value = "uid")
    private String uid;
    /**
     * 签到时间
     */
    @ExcelProperty(value = "签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;
    /**
     * 获得a币数量
     */
    @ExcelProperty(value = "获得a币数量")
    private Long aCurrency;

}
