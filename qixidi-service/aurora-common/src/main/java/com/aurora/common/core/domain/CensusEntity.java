package com.aurora.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计查询bo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CensusEntity {
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    @ApiModelProperty(value = "关键字", required = true)
    private String keyword;

    @ApiModelProperty(value = "开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "月份", required = true)
    private String month;

    @ApiModelProperty(value = "年份", required = true)
    private String year;

    @ApiModelProperty(value = "状态", required = true)
    private Integer state;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private Map<String, Object> params = new HashMap<>();
}
