package com.qixidi.business.domain.vo.label;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.label.LabelGroupingInfo;


/**
 * 标签分组信息视图对象 b_label_grouping_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class LabelGroupingInfoVo extends LabelGroupingInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 文章数
     */
    private Long articleNumber;

}
