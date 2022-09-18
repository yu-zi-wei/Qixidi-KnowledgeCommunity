package com.aurora.business.domain.bo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章排序类型Bo")
public class SortTypeBo {
    @ApiModelProperty(value = "浏览次数", required = true)
    Long numberTimes;

    @ApiModelProperty(value = "点赞次数", required = true)
    Long likeTimes;

    @ApiModelProperty(value = "创建时间", required = true)
    Long createTime;
}
