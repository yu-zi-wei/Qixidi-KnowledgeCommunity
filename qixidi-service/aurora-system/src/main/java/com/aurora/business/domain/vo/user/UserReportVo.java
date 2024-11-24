package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("用户签到视图对象")
@ExcelIgnoreUnannotated
@Accessors(chain = true)
public class UserReportVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * uid
     */
    @ExcelProperty(value = "uid")
    @ApiModelProperty("uid")
    private String uid;

    /**
     * 签到时间
     */
    @ExcelProperty(value = "签到时间")
    @ApiModelProperty("签到时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;

    /**
     * 获得a币数量
     */
    @ExcelProperty(value = "获得a币数量")
    @ApiModelProperty("获得a币数量")
    private Long acurrency;


}
