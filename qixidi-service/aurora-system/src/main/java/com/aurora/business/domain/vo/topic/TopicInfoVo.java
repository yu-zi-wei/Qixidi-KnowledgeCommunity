package com.aurora.business.domain.vo.topic;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 话题信息视图对象 topic_infor
 *
 * @author aurora
 * @date 2023-03-09
 */
@Data
@ApiModel("话题信息视图对象")
@ExcelIgnoreUnannotated
public class TopicInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 话题名称
     */
    @ExcelProperty(value = "话题名称")
    @ApiModelProperty("话题名称")
    private String topicName;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    @ApiModelProperty("描述")
    private String describe;

    /**
     * 顺序
     */
    @ExcelProperty(value = "顺序")
    @ApiModelProperty("顺序")
    private Long order;

    /**
     * 状态（0：开启，1：已结束）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：开启，1：已结束")
    @ApiModelProperty("状态（0：开启，1：已结束）")
    private Long state;

    /**
     * 参与人数
     */
    @ExcelProperty(value = "参与人数")
    @ApiModelProperty("参与人数")
    private Long number;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间")
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ExcelProperty(value = "结束时间")
    @ApiModelProperty("结束时间")
    private Date endTime;


}
