package com.qixidi.business.domain.entity.shield;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 屏蔽词库对象 to_shield_word
 *
 * @author aurora
 * @date 2023-02-20
 */
@Data
@TableName("to_shield_word")
public class ToShieldWord {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    @ExcelProperty(value = "")
    private Long id;
    /**
     * 屏蔽字
     */
    @ExcelProperty(value = "屏蔽字")
    private String keyword;
    /**
     * 类型（1：文章，2：评论）
     */
    @ExcelProperty(value = "类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2：评论")
    private Long type;
    /**
     * 屏蔽词权重
     */
    @ExcelProperty(value = "屏蔽词权重")
    private Long weight;
    /**
     * 状态（0：正常，1：已失效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已失效")
    private Long state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建名
     */
    private String createBy;

}
