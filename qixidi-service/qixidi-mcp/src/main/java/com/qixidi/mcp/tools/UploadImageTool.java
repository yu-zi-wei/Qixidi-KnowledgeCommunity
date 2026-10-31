package com.qixidi.mcp.tools;

import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.helper.OperLogRecorder;
import com.qixidi.system.service.ISysOssService;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.mcp.domain.vo.image.McpUploadImageVo;
import com.qixidi.mcp.utils.ByteArrayMultipartFile;
import com.qixidi.system.domain.vo.SysOssUploadVo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

/**
 * 图片上传 tool：agent 生成/挑选的配图经 base64 上传到站内 OSS，返回 url 供文章封面与正文引用
 *
 * @author zi-wei
 */
@Component
@RequiredArgsConstructor
public class UploadImageTool {

    /** 单图上限 10MB（防无界内存与 OSS 垃圾写入） */
    private static final int MAX_IMAGE_BYTES = 10 * 1024 * 1024;

    private final ISysOssService iSysOssService;
    private final OperLogRecorder operLogRecorder;

    /**
     * 上传前置校验：大小上限 + 图片魔数嗅探（png/jpg/gif/webp）
     */
    static void validateImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new ServiceException("图片内容为空");
        }
        if (bytes.length > MAX_IMAGE_BYTES) {
            throw new ServiceException("图片超过 10MB 上限");
        }
        if (!isImageMagic(bytes)) {
            throw new ServiceException("仅支持图片（png/jpg/gif/webp），请确认 base64 内容是图片");
        }
    }

    private static boolean isImageMagic(byte[] b) {
        // PNG
        if (startsWith(b, new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A})) {
            return true;
        }
        // JPEG
        if (startsWith(b, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})) {
            return true;
        }
        // GIF87a / GIF89a
        if (startsWith(b, "GIF8".getBytes(StandardCharsets.US_ASCII))) {
            return true;
        }
        // WEBP：RIFF????WEBP
        return startsWith(b, "RIFF".getBytes(StandardCharsets.US_ASCII))
            && startsWith(b, "WEBP".getBytes(StandardCharsets.US_ASCII), 8);
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        return startsWith(data, prefix, 0);
    }

    private static boolean startsWith(byte[] data, byte[] prefix, int offset) {
        if (data.length < offset + prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[offset + i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    @Tool(name = "upload_image", description = "上传图片（base64 编码），返回 OSS url，用于文章封面与正文配图。支持 png/jpg/jpeg/gif/webp，单图不超过 10MB。")
    public McpUploadImageVo uploadImage(
        @ToolParam(description = "图片内容的 base64 字符串（不含 data:image/xxx;base64, 前缀），解码后不超过 10MB") String base64,
        @ToolParam(description = "文件名，带后缀（如 cover.png），后缀限 png/jpg/jpeg/gif/webp，缺省 mcp-image.png", required = false) String filename
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
        validateImage(bytes);
        String name = (filename == null || filename.isBlank()) ? "mcp-image.png" : filename.trim();
        SysOssUploadVo oss = iSysOssService.upload(new ByteArrayMultipartFile("file", name, guessContentType(name), bytes));
        operLogRecorder.record("MCP文件", "上传图片", BusinessType.INSERT,
            user.getUsername(), "fileName=" + name + ", ossId=" + oss.getOssId(), null);
        McpUploadImageVo result = new McpUploadImageVo();
        result.setUrl(oss.getUrl());
        result.setOssId(oss.getOssId());
        result.setFileName(oss.getOriginalName());
        return result;
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
