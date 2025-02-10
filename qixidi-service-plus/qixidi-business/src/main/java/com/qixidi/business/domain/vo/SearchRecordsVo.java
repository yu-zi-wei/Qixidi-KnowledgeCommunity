package com.qixidi.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 搜索记录视图对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */
@Data
@ExcelIgnoreUnannotated
public class SearchRecordsVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;


}

