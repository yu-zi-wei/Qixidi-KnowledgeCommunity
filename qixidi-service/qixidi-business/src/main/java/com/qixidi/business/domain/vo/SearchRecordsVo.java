package com.qixidi.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.SearchRecords;


/**
 * 搜索记录视图对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */
@Data
@ExcelIgnoreUnannotated
public class SearchRecordsVo extends SearchRecords {

    private static final long serialVersionUID = 1L;


}

