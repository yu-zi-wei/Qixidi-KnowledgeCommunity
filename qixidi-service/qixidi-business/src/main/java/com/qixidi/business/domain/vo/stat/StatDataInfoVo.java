package com.qixidi.business.domain.vo.stat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import com.qixidi.business.domain.entity.stat.StatDataInfo;


/**
 * 统计数据信息视图对象 b_stat_data_info
 *
 * @author aurora
 * @date 2023-03-14
 */
@Data
@ExcelIgnoreUnannotated
public class StatDataInfoVo extends StatDataInfo {
    private static final long serialVersionUID = 1L;

    /**
     * 分类数
     */
    private Long labelGroupCount;

    /**
     * 标签数
     */
    private Long labelCount;

}
