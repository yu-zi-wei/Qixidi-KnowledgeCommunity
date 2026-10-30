package com.qixidi.business.domain.vo.special;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.special.SpecialInformation;


/**
 * 专栏信息视图对象 b_special_information
 *
 * @author aurora
 * @date 2022-08-19
 */
@Data
@ExcelIgnoreUnannotated
public class SpecialInformationVo extends SpecialInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String username;

    /**
     * 收录数
     */
    private Integer includedCount;
}

