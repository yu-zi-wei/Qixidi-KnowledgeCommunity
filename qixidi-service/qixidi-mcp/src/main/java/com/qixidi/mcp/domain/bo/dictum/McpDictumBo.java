package com.qixidi.mcp.domain.bo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔内容入参对象（MCP create_dictum / update_dictum 共用）
 *
 * Spring AI 对 POJO 工具参数递归生成 object schema：
 * 字段描述与必填性统一走 @ToolParam——字段默认 required（SpringAiSchemaModule
 * requiredByDefault=true），可选字段必须显式 required = false，否则 schema 的
 * required 数组会全量误导调用方
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToolParam(required = false, description = "随笔内容。create_dictum 必填；update_dictum 不修改内容可不传")
    private String content;

    @ToolParam(required = false, description = "分类 id，取值来自 get_dictum_context.groups。create_dictum 必填；update_dictum 不修改可不传")
    private Long groupId;

    @ToolParam(required = false, description = "专辑 id，取值来自 get_dictum_context.albums；不归入专辑可不传")
    private Long albumId;

    @ToolParam(required = false, description = "标签，自由文本，多个以英文逗号隔开（如：人生,读书）")
    private String label;

    @ToolParam(required = false, description = "随笔出处作者（摘抄场景填原作者，如：鲁迅），可空")
    private String author;

    @ToolParam(required = false, description = "随笔出处作品名（如：朝花夕拾），可空")
    private String worksName;

    @ToolParam(required = false, description = "配图 url，多个以英文逗号隔开（来自 upload_image 返回值），可空")
    private String picture;

    @ToolParam(required = false, description = "可见状态：1 公开（默认）、2 私有、3 关注可看。create_dictum 必填；update_dictum 不修改可不传")
    private Integer dictumState;

    @ToolParam(required = false, description = "随笔 id：update_dictum 必传（id 来自 search_dictums 或 create_dictum 返回值），新建不传")
    private Long dictumId;
}
