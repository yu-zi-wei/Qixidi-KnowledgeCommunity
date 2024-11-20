package com.aurora.business.domain.vo.topic;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 话题参与信息视图对象 topic_join_info
 *
 * @author aurora
 * @date 2023-03-09
 */
@Data
@ApiModel("话题参与信息视图对象")
@ExcelIgnoreUnannotated
public class TopicJoinInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String uid;

    /**
     * 话题id
     */
    @ExcelProperty(value = "话题id")
    @ApiModelProperty("话题id")
    private Long topicId;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @ApiModelProperty("内容")
    private String content;

    /**
     * 附件
     */
    @ExcelProperty(value = "附件")
    @ApiModelProperty("附件")
    private String enclosure;

    /**
     * 附件类型
     */
    @ExcelProperty(value = "附件类型")
    @ApiModelProperty("附件类型")
    private String enclosureType;

    /**
     * 状态（0：正常，1：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已删除")
    @ApiModelProperty("状态（0：正常，1：已删除）")
    private Long state;


}
