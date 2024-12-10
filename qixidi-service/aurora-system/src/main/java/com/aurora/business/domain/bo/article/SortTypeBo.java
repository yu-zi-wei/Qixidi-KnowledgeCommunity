package com.aurora.business.domain.bo.article;

import lombok.Data;

/**
 * 文章排序类型Bo
 */
@Data
public class SortTypeBo {
    /**
     * 用户id
     */
    String uid;
    /**
     * 浏览次数
     */
    Long numberTimes;
    /**
     * 点赞次数
     */
    Long likeTimes;
    /**
     * 创建时间
     */
    Long createTime;
    /**
     * 排序规则（1：权重，2：时间）
     */
    Integer sortType = 1;
    /**
     * 标签分组id
     */
    Long groupingId;
}
