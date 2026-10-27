package com.qixidi.mcp.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.light.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * agent 接入 API key
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("b_agent_api_key")
public class AgentApiKey extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户 uuid
     */
    private String userId;

    /**
     * 备注名（如 workbuddy）
     */
    private String keyName;

    /**
     * key 明文的 SHA-256（小写 hex），明文仅创建时返回一次
     */
    private String keyHash;

    /**
     * 状态（1 启用 0 停用，预留）
     */
    private Integer status;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 最后使用时间（约 5 分钟缓存粒度）
     */
    private Date lastUsedTime;
}
