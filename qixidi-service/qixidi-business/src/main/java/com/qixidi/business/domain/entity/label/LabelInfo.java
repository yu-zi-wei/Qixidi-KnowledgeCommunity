package com.qixidi.business.domain.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 标签信息对象 b_label_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@TableName("b_label_info")
public class LabelInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 分组id
     */
    @ExcelProperty(value = "分组id")
    private Long labelGroupingId;
    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String labelName;
    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String labelDescribe;
    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String labelCover;
    /**
     * 关注数
     */
    private Integer followNumber;
    /**
     * 文章数
     */
    private Integer articleNumber;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    private Integer state;

    private Long createBy;

    @ExcelDictFormat(readConverterExp = "创建时间")
    private Date createTime;

    public String getIdStr() {
        return id.toString();
    }
}

