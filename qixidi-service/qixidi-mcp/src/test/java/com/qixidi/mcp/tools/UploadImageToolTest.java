package com.qixidi.mcp.tools;

import com.light.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * upload_image 入参校验：大小上限 + 图片魔数嗅探（非图片/超大须明确报错，不得写 OSS）
 *
 * @author zi-wei
 */
class UploadImageToolTest {

    private static final int MAX = 10 * 1024 * 1024;

    @Test
    void rejectsOversizedImage() {
        byte[] bytes = new byte[MAX + 1];
        System.arraycopy(pngMagic(), 0, bytes, 0, 8);
        assertThatThrownBy(() -> UploadImageTool.validateImage(bytes))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("10MB");
    }

    @Test
    void rejectsNonImageBytes() {
        assertThatThrownBy(() -> UploadImageTool.validateImage(
                "<html>not an image</html>".getBytes(StandardCharsets.UTF_8)))
            .isInstanceOf(ServiceException.class)
            .hasMessageContaining("仅支持图片");
    }

    @Test
    void acceptsPng() {
        assertThatCode(() -> UploadImageTool.validateImage(pngMagic())).doesNotThrowAnyException();
    }

    @Test
    void acceptsJpeg() {
        byte[] jpeg = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10};
        assertThatCode(() -> UploadImageTool.validateImage(jpeg)).doesNotThrowAnyException();
    }

    @Test
    void acceptsGif() {
        assertThatCode(() -> UploadImageTool.validateImage("GIF89a".getBytes(StandardCharsets.US_ASCII)))
            .doesNotThrowAnyException();
    }

    @Test
    void acceptsWebp() {
        byte[] webp = new byte[12];
        System.arraycopy("RIFF".getBytes(StandardCharsets.US_ASCII), 0, webp, 0, 4);
        System.arraycopy("WEBP".getBytes(StandardCharsets.US_ASCII), 0, webp, 8, 4);
        assertThatCode(() -> UploadImageTool.validateImage(webp)).doesNotThrowAnyException();
    }

    private static byte[] pngMagic() {
        return new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    }
}
