package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 专栏条目（get_publish_context.specials 行；id 用于 create_article 的 specialId 取值）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpSpecialItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;
}
