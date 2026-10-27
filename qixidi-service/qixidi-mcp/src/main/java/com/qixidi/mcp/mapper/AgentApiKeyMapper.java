package com.qixidi.mcp.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.mcp.domain.entity.AgentApiKey;
import com.qixidi.mcp.domain.vo.AgentApiKeyVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * agent 接入 API key
 *
 * @author zi-wei
 */
@Mapper
public interface AgentApiKeyMapper extends BaseMapperPlus<AgentApiKeyMapper, AgentApiKey, AgentApiKeyVo> {
}
