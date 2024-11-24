package com.aurora.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 搜索记录视图对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */
@Data
@ApiModel("搜索记录视图对象")
@ExcelIgnoreUnannotated
public class SearchRecordsVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String uid;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @ApiModelProperty("内容")
    private String content;
    private Date createTime;


}

