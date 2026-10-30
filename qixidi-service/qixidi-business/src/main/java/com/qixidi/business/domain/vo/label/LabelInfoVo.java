package com.qixidi.business.domain.vo.label;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.label.LabelInfo;


/**
 * 标签信息视图对象 b_label_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ExcelIgnoreUnannotated
public class LabelInfoVo extends LabelInfo {

    private static final long serialVersionUID = 1L;

    private String uid;

    /**
     * 是否关注
     */
    private Boolean isFollow = false;

    /**
     * 按钮状态
     */
    private Boolean buttonLoading = false;

    /**
     * 标签id
     */
    @ExcelProperty(value = "标签id")
    private String labelId;

    /**
     * 分组名称
     */
    @ExcelDictFormat(readConverterExp = "分组名称")
    private String groupingName;

}
