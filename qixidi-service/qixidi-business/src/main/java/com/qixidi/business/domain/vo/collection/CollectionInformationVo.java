package com.qixidi.business.domain.vo.collection;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.collection.CollectionInformation;


/**
 * 收藏夹信息视图对象 b_collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */
@Data
@ExcelIgnoreUnannotated
public class CollectionInformationVo extends CollectionInformation {

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

