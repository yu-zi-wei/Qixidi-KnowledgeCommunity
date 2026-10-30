package com.qixidi.business.domain.vo.fabulous;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;


/**
 * 点赞视图对象 b_fabulous_record
 *
 * @author aurora
 * @date 2022-10-01
 */
@Data
@ExcelIgnoreUnannotated
public class FabulousRecordVo extends FabulousRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String nickname;

}
