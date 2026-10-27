package com.qixidi.mcp.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API key 创建入参
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AgentApiKeyBo {

    /**
     * 备注名（如 workbuddy）
     */
    @NotBlank(message = "keyName不能为空")
    @Size(max = 64, message = "keyName长度不能超过64")
    private String keyName;

    /**
     * 有效期（天），空或非正数取默认 365
     */
    private Long validDays;
}
