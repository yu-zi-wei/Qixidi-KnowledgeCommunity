package com.qixidi.system.service;

import com.qixidi.system.domain.vo.SysOssUploadVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 服务层：按 qixidi.storage.provider 配置分流 MinIO/OSS 云存储，仅返回云对象信息不落库
 *
 * @author Lion Li
 */
public interface ISysOssService {

    /**
     * 上传文件到云存储
     */
    SysOssUploadVo upload(MultipartFile file);

}
