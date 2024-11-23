package com.aurora.lover.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import com.aurora.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 基本配置视图对象 lover_congfig
 *
 * @author ziwei
 * @date 2022-12-02
 */
@Data
@ExcelIgnoreUnannotated
public class LoverConfigVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * is
     */
    @ExcelProperty(value = "is")
    private Long id;

    /**
     * 邮箱（多个以逗号隔开）
     */
    @ExcelProperty(value = "邮箱", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    private String mail;

    /**
     * 备案号
     */
    @ExcelProperty(value = "备案号")
    private String filings;

    @ExcelProperty(value = "公安备案号")
    private String securityFilings;
    /**
     * 域名
     */
    @ExcelProperty(value = "域名")
    private String realmName;


}

