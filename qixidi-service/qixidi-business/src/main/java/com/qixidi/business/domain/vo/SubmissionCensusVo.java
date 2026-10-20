package com.qixidi.business.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投稿贡献按天统计（文章 + 小记）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionCensusVo {

    /**
     * 日期 yyyy-MM-dd
     */
    private String dateTimes;

    /**
     * 当日发布文章数
     */
    private Long articleSum;

    /**
     * 当日发布小记数
     */
    private Long noteSum;
}
