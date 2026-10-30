package com.qixidi.business.domain.entity.special;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 专栏信息对象 b_special_information
 *
 * @author aurora
 * @date 2022-08-19
 */
@Data
@TableName("b_special_information")
public class SpecialInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 专栏id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "专栏id")
    private Long id;
    /**
     * 专栏名称
     */
    @ExcelProperty(value = "专栏名称")
    private String specialName;
    /**
     * 专栏简介
     */
    @ExcelProperty(value = "专栏简介")
    private String specialIntroduce;
    /**
     * 封面
     */
    private String cover;
    /**
     * 专栏状态（0：正常，1：未启用）
     */
    @ExcelProperty(value = "专栏状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：未启用")
    private Integer state;
    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;
    /**
     * 修改者id
     */
    @ExcelProperty(value = "修改者id")
    private String updateId;

    private Date createTime;

    private Date updateTime;
}
