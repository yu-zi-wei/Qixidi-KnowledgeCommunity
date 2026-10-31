package com.qixidi.system.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传结果（云存储对象信息，无表承载——sys_oss 表已废弃）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class SysOssUploadVo {

    /** 云存储对象 id（MinIO/OSS 侧标识，非数据库主键） */
    private String ossId;

    /** 云存储文件名（含路径前缀） */
    private String fileName;

    /** 原始文件名 */
    private String originalName;

    /** 文件后缀（含 .） */
    private String fileSuffix;

    /** 访问 url */
    private String url;

    /** 存储服务商标识 */
    private String service;

}
