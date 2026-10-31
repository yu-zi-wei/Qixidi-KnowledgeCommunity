package com.qixidi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.light.core.constant.CloudStorageProvider;
import com.light.exception.ServiceException;
import com.light.minio.service.MinioService;
import com.light.oss.service.OssService;
import com.qixidi.system.domain.vo.SysOssUploadVo;
import com.qixidi.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 服务层实现：分流 MinIO / 阿里云 OSS，返回云对象信息（不落库，sys_oss 表已废弃）
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysOssServiceImpl implements ISysOssService {

    private final MinioService minioService;
    private final OssService ossService;

    @Value("${qixidi.storage.provider}")
    private String storageProvider;

    @Override
    public SysOssUploadVo upload(MultipartFile file) {
        SysOssUploadVo vo;
        if (CloudStorageProvider.MINIO.equals(storageProvider)) {
            vo = BeanUtil.copyProperties(minioService.upload(file), SysOssUploadVo.class);
        } else if (CloudStorageProvider.OSS.equals(storageProvider)) {
            vo = BeanUtil.copyProperties(ossService.upload(file), SysOssUploadVo.class);
        } else {
            throw new ServiceException("暂不支持该存储服务");
        }
        return vo;
    }

}
