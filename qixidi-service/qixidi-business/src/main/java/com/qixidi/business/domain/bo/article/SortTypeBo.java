package com.qixidi.business.domain.bo.article;

import lombok.Data;

/**
 * 文章排序类型Bo
 */
@Data
public class SortTypeBo {
    /**
     * 用户id
     */
    private String uid;
    /**
     * 浏览次数
     */
    private Long numberTimes;
    /**
     * 点赞次数
     */
    private Long likeTimes;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 排序规则（1：权重，2：时间）
     */
    private Integer sortType = 1;
    /**
     * 标签分组id
     */
    private Long groupingId;
}
