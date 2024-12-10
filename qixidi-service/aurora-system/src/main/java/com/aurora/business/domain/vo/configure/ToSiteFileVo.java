package com.aurora.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import lombok.Data;

import java.util.Date;


/**
 * 网站文档视图对象 to_site_file
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@ExcelIgnoreUnannotated
public class ToSiteFileVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * md内容
     */
    @ExcelProperty(value = "md内容")
    private String content;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private Long uid;

    /**
     * 状态（0：正常，1：已失效）
     */
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已失效")
    private Integer state;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 主题
     */
    private String theme;
}
