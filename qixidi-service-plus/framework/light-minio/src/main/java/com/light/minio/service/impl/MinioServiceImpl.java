package com.light.minio.service.impl;

import com.light.minio.config.MinioConfig;
import com.light.minio.domain.dto.MinioDto;
import com.light.minio.service.MinioService;
import com.luciad.imageio.webp.WebPWriteParam;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;


/**
 * @author zi-wei
 * @create 2025/2/20 14:58
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    //是否开启压缩
    private static final boolean compressionStatus = false;

    @SneakyThrows
    @Override
    public MinioDto upload(MultipartFile file) {
        String bucketName = minioConfig.getBucketName();

        //获取原生文件名
        String originalFilename = file.getOriginalFilename();
        //JDK8的日期格式
        LocalDateTime time = LocalDateTime.now();
        //拼装OSS上存储的路径
        String folder = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(time);
        String uuid = generateUUID();

        String extension = null;
        InputStream inputStream = null;
        String contentType = null;
        if (compressionStatus && isImage(originalFilename)) {//图片文件
            byte[] imageBytes = null;
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                WebPWriteParam writeParam = new WebPWriteParam(Locale.getDefault());
                writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
                writeParam.setCompressionQuality(0.8f);
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType("image/webp");
                if (!writers.hasNext()) {
                    throw new IllegalStateException("No writers found for WebP format");
                }
                ImageWriter writer = writers.next();

                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                    writer.setOutput(ios);
                    writer.write(null, new IIOImage(originalImage, null, null), writeParam);
                } finally {
                    writer.dispose();
                }
                imageBytes = baos.toByteArray();
            }
            extension = ".webp";//压缩为.webp
            inputStream = new ByteArrayInputStream(imageBytes);
            contentType = "image/webp";

        } else {//非图片文件
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            inputStream = file.getInputStream();
            contentType = file.getContentType();
        }
        //在minio上bucket下的文件名
        String uploadFileName = folder + "/" + uuid + extension;

        MinioDto dto = new MinioDto();
        dto.setOssId(uuid);
        dto.setFileName(uploadFileName);
        dto.setOriginalName(originalFilename);
        dto.setFileSuffix(extension);
        dto.setService("minio");

        try {
            //上传
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uploadFileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
            dto.setUrl(minioConfig.getAccessPort() + "/" + bucketName + "/" + uploadFileName);
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            log.error("上传文件失败，请核对Minio配置信息:[" + e.getMessage() + "]");
        }
        return dto;
    }

    // 检查文件是否为图片
    private boolean isImage(String fileName) {
        if (fileName == null) return false;
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") ||
                lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".gif") ||
                lowerCaseName.endsWith(".webp");
    }

    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

}
