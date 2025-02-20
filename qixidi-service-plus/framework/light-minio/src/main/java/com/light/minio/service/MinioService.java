package com.light.minio.service;

import com.light.minio.domain.dto.MinioDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zi-wei
 * @create 2025/2/20 14:58
 */
public interface MinioService {
    MinioDto upload(MultipartFile file);
}
