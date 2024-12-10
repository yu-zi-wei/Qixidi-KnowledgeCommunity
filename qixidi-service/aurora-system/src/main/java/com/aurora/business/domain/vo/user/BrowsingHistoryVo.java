package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 用户浏览历史
 * <p>
 * 视图对象 b_browsing_history
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class BrowsingHistoryVo {

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
     * 目标id
     */
    @ExcelProperty(value = "目标id")
    private Long targetId;

    /**
     * 目标标题
     */
    @ExcelProperty(value = "目标标题")
    private String targetTitle;

    /**
     * 目标uid
     */
    @ExcelProperty(value = "目标uid")
    private String targetUid;

    /**
     * 目标类型（1：文章，2：帖子）
     */
    @ExcelProperty(value = "目标类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2：帖子")
    private Integer targetType;

    private Date createTime;

    private Date updateTime;

}

