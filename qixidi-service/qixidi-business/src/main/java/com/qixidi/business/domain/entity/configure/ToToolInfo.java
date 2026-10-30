package com.qixidi.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 工具信息对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@TableName("to_tool_info")
public class ToToolInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 父级id
     */
    @ExcelProperty(value = "父级id")
    private Long parentId;
    /**
     * 工具名称
     */
    @ExcelProperty(value = "工具名称")
    private String toolName;
    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icon;
    /**
     * 是否为父级（1；父级，2：子级）
     */
    @ExcelProperty(value = "是否为父级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1；父级，2：子级")
    private Integer isParent;
    /**
     * 工具地址
     */
    @ExcelProperty(value = "工具地址")
    private String toolUrl;
    /**
     * 排序
     */
    @TableField("`order`")
    @ExcelProperty(value = "排序")
    private Long order;
    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private Long type;
    /**
     * 状态（0：正常，1：已失效）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已失效")
    private Integer state;
    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人")
    private String createBy;

    private Date createTime;

}
