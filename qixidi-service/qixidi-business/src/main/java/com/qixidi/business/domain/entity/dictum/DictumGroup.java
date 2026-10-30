package com.qixidi.business.domain.entity.dictum;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 名言分组对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_dictum_group")
@Accessors(chain = true)
public class DictumGroup {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelProperty(value = "id")
    private Long id;
    ;
    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    private String name;
    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String cover;
    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String briefIntroduction;
    /**
     * 状态（0：正常吗：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常吗：已删除")
    private Integer state;

    private Date createTime;

    private Date updateTime;

}

