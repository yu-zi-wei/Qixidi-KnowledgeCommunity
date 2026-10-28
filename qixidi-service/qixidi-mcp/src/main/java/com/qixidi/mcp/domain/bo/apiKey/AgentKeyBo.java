package com.qixidi.mcp.domain.bo.apiKey;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * API key 创建/改名请求体（validDays 仅创建时生效）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AgentKeyBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备注名（如 workbuddy）
     */
    @NotBlank(message = "keyName不能为空")
    @Size(max = 64, message = "keyName长度不能超过64")
    private String keyName;

    /**
     * 有效期天数（缺省 365，仅创建时使用）
     */
    private Long validDays;
}
