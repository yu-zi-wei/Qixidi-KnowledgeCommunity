package com.aurora.business.domain.bo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章排序类型Bo")
public class SortTypeBo {
    @ApiModelProperty(value = "浏览次数", required = true)
    String uid;

    @ApiModelProperty(value = "浏览次数", required = true)
    Long numberTimes;

    @ApiModelProperty(value = "点赞次数", required = true)
    Long likeTimes;

    @ApiModelProperty(value = "创建时间", required = true)
    Long createTime;

    @ApiModelProperty(value = "排序规则（1：权重，2：时间）", required = true)
    Integer sortType = 1;

    @ApiModelProperty(value = "标签分组id", required = true)
    Long groupingId;
}
