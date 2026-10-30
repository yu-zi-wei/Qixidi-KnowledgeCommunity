package com.qixidi.business.domain.vo.collection;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import com.qixidi.business.domain.entity.collection.CollectionRecord;


/**
 * 文章收藏关联视图对象 b_collection_record
 *
 * @author aurora
 * @date 2022-11-10
 */
@Data
@ExcelIgnoreUnannotated
public class CollectionRecordVo extends CollectionRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    private String createBy;

}
