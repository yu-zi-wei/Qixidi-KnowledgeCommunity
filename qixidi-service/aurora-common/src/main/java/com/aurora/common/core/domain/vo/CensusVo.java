package com.aurora.common.core.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 统计查询bo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CensusVo {

    private Long id;
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    @ApiModelProperty(value = "时间", required = true)
    private Date dateTime;

    @ApiModelProperty(value = "时间", required = true)
    private String dateTimes;

    @ApiModelProperty(value = "总数", required = true)
    private Long censusSum = 0L;

}
