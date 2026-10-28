package com.qixidi.mcp.domain.vo.image;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片上传结果（MCP upload_image 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpUploadImageVo implements Serializable {

    /**
     * 图片访问 url（用于文章封面与正文引用）
     */
    private String url;

    /**
     * OSS 文件 id
     */
    private String ossId;

    /**
     * 文件名
     */
    private String fileName;
}
