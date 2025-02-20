package com.light.oss.service.Impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.light.oss.config.OSSConfig;
import com.light.oss.domain.dto.OssDto;
import com.light.oss.service.OssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author zi-wei
 * @create 2025/2/20 14:46
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OSSConfigImpl implements OssService {

    private final OSSConfig ossConfig;

    @Override
    public OssDto upload(MultipartFile file) {
        String bucketName = ossConfig.getBucketName();
        String endPoint = ossConfig.getEndPoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String httpProtocol = ossConfig.getHttpProtocol();

        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        //获取原生文件名
        String originalFilename = file.getOriginalFilename();
        //JDK8的日期格式
        LocalDateTime time = LocalDateTime.now();
        //拼装OSS上存储的路径
        String folder = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(time);
        String uuid = generateUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //在OSS上bucket下的文件名
        String uploadFileName = folder + "/" + uuid + extension;

        OssDto oss = new OssDto();
        oss.setOssId(uuid);
        oss.setFileName(uploadFileName);
        oss.setOriginalName(originalFilename);
        oss.setFileSuffix(extension);
        oss.setService("aliyun");

        try {
            PutObjectResult result = ossClient.putObject(bucketName, uploadFileName, file.getInputStream());
            //拼装返回路径
            if (result != null) {
                String url = httpProtocol + "://" + bucketName + "." + endPoint + "/" + uploadFileName;
                oss.setUrl(url);
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}", e.getMessage());
        } finally {
            //OSS关闭服务
            ossClient.shutdown();
        }
        return oss;
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

}
