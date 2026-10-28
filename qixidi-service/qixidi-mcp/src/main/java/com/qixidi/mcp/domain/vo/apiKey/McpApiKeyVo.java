package com.qixidi.mcp.domain.vo.apiKey;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * API key 管理列表 VO（脱敏：不含 keyHash，明文仅创建时返回一次）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpApiKeyVo implements Serializable {

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
