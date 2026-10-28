package com.qixidi.mcp.domain.vo.apiKey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

    private Long id;

    /**
     * 备注名
     */
    private String keyName;

    /**
     * 状态（1 启用 0 停用）
     */
    private Integer status;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 最后使用时间
     */
    private Date lastUsedTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
