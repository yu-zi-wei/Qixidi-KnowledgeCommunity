package com.aurora.business.domain.vo;

import lombok.Data;

/**
 * @author zi-wei
 * @create 2024/11/17 17:38
 */
@Data
public class FeedbackStatusSumVo {
    /**
     * 开启的(待处理)
     */
    private Long toBeProcessed = 0L;
    /**
     * 进行中
     */
    private Long underWay = 0L;
    /**
     * 已完成
     */
    private Long completed = 0L;
    /**
     * 已关闭
     */
    private Long closed = 0L;

    /**
     * 所有数据
     */
    private Long allData = 0L;
}
