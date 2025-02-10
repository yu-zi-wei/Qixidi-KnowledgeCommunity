package com.light.core.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    /**
     * 用户id
     */
    private String uid;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 月份
     */
    private String month;
    /**
     * 年份
     */
    private String year;
    /**
     * 状态
     */
    private Integer state;

    /**
     * 请求参数
     */
    private Map<String, Object> params = new HashMap<>();
}
