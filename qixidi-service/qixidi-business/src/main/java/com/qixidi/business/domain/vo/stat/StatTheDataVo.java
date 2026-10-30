package com.qixidi.business.domain.vo.stat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import com.qixidi.business.domain.entity.stat.StatTheData;


/**
 * 网站数据月统计视图对象 b_stat_data
 *
 * @author aurora
 * @date 2023-03-14
 */
@Data
@ExcelIgnoreUnannotated
public class StatTheDataVo extends StatTheData {

    private static final long serialVersionUID = 1L;


}
