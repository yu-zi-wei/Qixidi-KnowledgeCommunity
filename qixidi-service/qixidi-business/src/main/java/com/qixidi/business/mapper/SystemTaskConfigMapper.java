package com.qixidi.business.mapper;

import com.qixidi.business.domain.entity.SystemTaskConfig;
import com.qixidi.business.domain.vo.SystemTaskConfigVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author zi-wei
 * @create 2024/11/17 14:03
 */
@Mapper
public interface SystemTaskConfigMapper extends BaseMapperPlus<SystemTaskConfigMapper, SystemTaskConfig, SystemTaskConfigVo> {

    @Update("update b_system_task_config set task_execution_sum = task_execution_sum + 1 where id = #{taskId}")
    int addExecutionSum(Integer taskId);
}
