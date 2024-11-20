package com.aurora.business.mapper;

import com.aurora.business.domain.entity.SystemTaskConfig;
import com.aurora.business.domain.vo.SystemTaskConfigVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author zi-wei
 * @create 2024/11/17 14:03
 */
@Mapper
public interface SystemTaskConfigMapper extends BaseMapperPlus<SystemTaskConfigMapper, SystemTaskConfig, SystemTaskConfigVo> {

    @Update("update system_task_config set task_execution_sum = task_execution_sum + 1 where id = #{taskId}")
    int addExecutionSum(Integer taskId);
}
