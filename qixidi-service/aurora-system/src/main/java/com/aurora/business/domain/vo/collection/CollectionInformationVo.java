package com.aurora.business.domain.vo.collection;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 收藏夹信息视图对象 collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */
@Data
@ApiModel("收藏夹信息视图对象")
@ExcelIgnoreUnannotated
public class CollectionInformationVo {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏夹id
     */
    @ExcelProperty(value = "收藏夹id")
    @ApiModelProperty("收藏夹id")
    private Long id;

    /**
     * 收藏夹名称
     */
    @ExcelProperty(value = "收藏夹名称")
    @ApiModelProperty("收藏夹名称")
    private String collectionName;

    /**
     * 收藏夹简介
     */
    @ExcelProperty(value = "收藏夹简介")
    @ApiModelProperty("收藏夹简介")
    private String collectionIntroduce;


    /**
     * 收藏夹状态（0：正常，1：未启用）
     */
    @ExcelProperty(value = "收藏夹状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：未启用")
    @ApiModelProperty("收藏夹状态（0：正常，1：未启用）")
    private Long state;

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

    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 收录数
     */
    private Integer includedCount;
}

