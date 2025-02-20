package com.light.oss.service;

import com.light.oss.domain.dto.OssDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zi-wei
 * @create 2025/2/20 14:45
 */
public interface OssService {
    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    OssDto upload(MultipartFile file);
}
