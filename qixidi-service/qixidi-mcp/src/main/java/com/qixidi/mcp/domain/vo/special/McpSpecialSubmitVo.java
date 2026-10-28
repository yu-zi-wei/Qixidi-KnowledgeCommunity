package com.qixidi.mcp.domain.vo.special;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新建专栏返回（MCP create_special）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpSpecialSubmitVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 专栏 id，可直接用于 create_article 的 specialId */
    private Long specialId;

    /** 专栏名称 */
    private String name;
}
