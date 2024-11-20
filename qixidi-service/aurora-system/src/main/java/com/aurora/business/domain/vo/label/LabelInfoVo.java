package com.aurora.business.domain.vo.label;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 标签信息视图对象 label_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@ApiModel("标签信息视图对象")
@ExcelIgnoreUnannotated
public class LabelInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    private String uid;

    /**
     * 是否关注
     */
    private Boolean isFollow = false;

    /**
     * 按钮状态
     */
    private Boolean buttonLoading = false;

    /**
     * 分组id
     */
    @ExcelProperty(value = "分组id")
    @ApiModelProperty("分组id")
    private Long labelGroupingId;

    /**
     * 标签名称
     */
    @ExcelProperty(value = "名称")
    @ApiModelProperty("名称")
    private String labelName;

    /**
     * 标签id
     */
    @ExcelProperty(value = "标签id")
    @ApiModelProperty("标签id")
    private String labelId;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    @ApiModelProperty("描述")
    private String labelDescribe;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    @ApiModelProperty("封面")
    private String labelCover;

    @ApiModelProperty("关注数")
    private Integer followNumber=0;

    @ApiModelProperty("文章数")
    private Integer articleNumber;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    @ApiModelProperty("状态（0：正常，1：已删除）")
    private Integer state;

    private Long createBy;

    @ExcelDictFormat(readConverterExp = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 分组名称
     */
    @ExcelDictFormat(readConverterExp = "分组名称")
    @ApiModelProperty("分组名称")
    private String groupingName;

}
