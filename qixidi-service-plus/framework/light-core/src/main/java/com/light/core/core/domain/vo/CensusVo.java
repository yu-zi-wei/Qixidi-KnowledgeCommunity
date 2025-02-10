package com.light.core.core.domain.vo;

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
    /**
     * "标题"
     */
    private String title;
    /**
     * 时间
     */
    private Date dateTime;
    /**
     * 时间
     */
    private String dateTimes;
    /**
     * 总数
     */
    private Long censusSum = 0L;

}
