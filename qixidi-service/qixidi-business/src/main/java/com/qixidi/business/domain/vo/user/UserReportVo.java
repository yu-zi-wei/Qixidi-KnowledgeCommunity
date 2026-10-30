package com.qixidi.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import com.qixidi.business.domain.entity.user.UserReport;


/**
 * 用户签到视图对象 b_user_report
 *
 * @author aurora
 * @date 2023-04-10
 */
@Data
@ExcelIgnoreUnannotated
@Accessors(chain = true)
public class UserReportVo extends UserReport {

    private static final long serialVersionUID = 1L;


}
