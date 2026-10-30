package com.qixidi.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;


/**
 * 搜索记录对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */
@Data
@TableName("b_search_records")
public class SearchRecords {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
