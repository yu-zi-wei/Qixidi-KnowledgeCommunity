package com.aurora.business.domain.vo.collection;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 文章收藏关联视图对象 collection_record
 *
 * @author aurora
 * @date 2022-11-10
 */
@Data
@ApiModel("文章收藏关联视图对象")
@ExcelIgnoreUnannotated
public class CollectionRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 目标id
     */
    @ExcelProperty(value = "目标id")
    @ApiModelProperty("目标id")
    private Long targetId;

    /**
     * 收藏夹id
     */
    @ExcelProperty(value = "收藏夹id")
    @ApiModelProperty("收藏夹id")
    private Long collectionId;

    /**
     * 收藏类型
     */
    @ExcelProperty(value = "收藏类型")
    @ApiModelProperty("收藏类型")
    private Integer type;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    @ApiModelProperty("状态（0：正常，1：已删除）")
    private Integer state;

    /**
     * 创建者id
     */
    @ExcelProperty(value = "创建者id")
    @ApiModelProperty("创建者id")
    private String uid;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
