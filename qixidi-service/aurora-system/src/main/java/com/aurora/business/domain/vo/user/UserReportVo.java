package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 用户签到视图对象 b_user_report
 *
 * @author aurora
 * @date 2023-04-10
 */
@Data
@ExcelIgnoreUnannotated
@Accessors(chain = true)
public class UserReportVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
    private Long acurrency;


}
