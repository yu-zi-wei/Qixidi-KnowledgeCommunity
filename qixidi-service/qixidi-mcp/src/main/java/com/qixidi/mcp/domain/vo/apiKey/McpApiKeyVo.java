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

    private String keyName;

    private Integer status;

    private Date expireTime;

    private Date lastUsedTime;

    private Date createTime;
}
