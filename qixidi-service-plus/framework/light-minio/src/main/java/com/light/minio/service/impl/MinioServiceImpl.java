package com.light.minio.service.impl;

import com.light.minio.config.MinioConfig;
import com.light.minio.domain.dto.MinioDto;
import com.light.minio.service.MinioService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        if (isImage(originalFilename)) {//图片文件
            extension = ".jpg";//压缩为.webp
            // 使用Thumbnailator处理图片
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .scale(1.0)  // 可调整缩放比例
                    .outputQuality(0.8)  // 压缩质量，0.8表示80%质量
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);
            // 准备转换后的数据流
            byte[] imageBytes = outputStream.toByteArray();
            inputStream = new ByteArrayInputStream(imageBytes);
            contentType = "image/jpg";

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
