package com.qixidi.mcp.tools;

import cn.hutool.json.JSONUtil;
import com.light.exception.ServiceException;
import com.qixidi.system.service.ISysOssService;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.mcp.utils.ByteArrayMultipartFile;
import com.qixidi.system.domain.entity.SysOss;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 图片上传 tool：agent 生成/挑选的配图经 base64 上传到站内 OSS，返回 url 供文章封面与正文引用
 *
 * @author zi-wei
 */
@Component
@RequiredArgsConstructor
public class UploadImageTool {

    private final ISysOssService iSysOssService;

    @Tool(name = "upload_image", description = "上传图片（base64 编码），返回 OSS url，用于文章封面与正文配图。支持 png/jpg/jpeg/gif/webp。")
    public String uploadImage(
        @ToolParam(description = "图片内容的 base64 字符串（不含 data:image/xxx;base64, 前缀）") String base64,
        @ToolParam(description = "文件名（带后缀，如 cover.png）", required = false) String filename
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("base64 内容无效，请传纯 base64（不含 data:image 前缀）");
        }
        String name = (filename == null || filename.isBlank()) ? "mcp-image.png" : filename.trim();
        SysOss oss = iSysOssService.upload(new ByteArrayMultipartFile("file", name, guessContentType(name), bytes));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("url", oss.getUrl());
        result.put("ossId", oss.getOssId());
        result.put("fileName", oss.getOriginalName());
        return JSONUtil.toJsonStr(result);
    }

    private String guessContentType(String filename) {
        String lower = filename.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
