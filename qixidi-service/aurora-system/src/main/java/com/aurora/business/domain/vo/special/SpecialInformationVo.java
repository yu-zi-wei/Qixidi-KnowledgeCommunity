package com.aurora.business.domain.vo.special;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 专栏信息视图对象 special_information
 *
 * @author aurora
 * @date 2022-08-19
 */
@Data
@ApiModel("专栏信息视图对象")
@ExcelIgnoreUnannotated
public class SpecialInformationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 专栏id
     */
    @ExcelProperty(value = "专栏id")
    @ApiModelProperty("专栏id")
    private Long id;

    /**
     * 专栏名称
     */
    @ExcelProperty(value = "专栏名称")
    @ApiModelProperty("专栏名称")
    private String specialName;

    /**
     * 专栏简介
     */
    @ExcelProperty(value = "专栏简介")
    @ApiModelProperty("专栏简介")
    private String specialIntroduce;
    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;
    /**
     * 专栏状态（0：正常，1：未启用）
     */
    @ExcelProperty(value = "专栏状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：未启用")
    @ApiModelProperty("专栏状态（0：正常，1：未启用）")
    private Integer state;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String uid;

    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    @ApiModelProperty("用户名称")
    private String username;

    /**
     * 修改者id
     */
    @ExcelProperty(value = "修改者id")
    @ApiModelProperty("修改者id")
    private String updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 收录数
     */
    private Integer includedCount;
}

