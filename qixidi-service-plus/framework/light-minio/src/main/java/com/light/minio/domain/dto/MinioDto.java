package com.light.minio.domain.dto;

import lombok.Data;

/**
 * @author zi-wei
 * @create 2025/2/20 14:47
 */
@Data
public class MinioDto {
    /**
     * 对象存储主键
     */
    private String ossId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;

    /**
     * 服务商
     */
    private String service;
}
