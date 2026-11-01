package com.qixidi.business.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zi-wei
 * @create 2024/11/17 13:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_system_task_config")
public class SystemTaskConfig {

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
     * 最新执行时间（addExecutionSum 与次数一并刷新）
     */
    private Date taskLastExecutionTime;

    /**
     * 接口地址
     */
    private String taskUrl;

}
