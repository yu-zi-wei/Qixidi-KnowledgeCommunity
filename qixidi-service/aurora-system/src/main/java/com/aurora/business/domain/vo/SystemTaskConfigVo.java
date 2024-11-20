package com.aurora.business.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zi-wei
 * @create 2024/11/17 13:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemTaskConfigVo {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务执行时间
     */
    private String taskExecutionTime;

    /**
     * 任务执行此次数
     */
    private String taskExecutionSum;

    /**
     * 接口地址
     */
    private String taskUrl;

}
