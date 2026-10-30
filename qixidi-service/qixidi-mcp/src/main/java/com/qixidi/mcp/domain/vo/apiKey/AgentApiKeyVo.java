package com.qixidi.mcp.domain.vo.apiKey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.qixidi.mcp.domain.entity.AgentApiKey;

/**
 * API key 视图（不回显 keyHash 与任何 key 明文）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AgentApiKeyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    private Date createTime;
}
