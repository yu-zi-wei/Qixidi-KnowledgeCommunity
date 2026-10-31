package com.qixidi.system.api;


import cn.hutool.core.util.ObjectUtil;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.system.domain.vo.SysOssUploadVo;
import com.qixidi.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传（门户编辑器配图 / 前后台头像 / MCP 上传共用入口；sys_oss 表及其管理接口已废弃）
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss")
@LogModule("文件")
public class SysOssController {

    private final ISysOssService iSysOssService;

    /**
     * 上传文件到云存储
     */
    @Log(title = "上传文件", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOssUploadVo oss = iSysOssService.upload(file);
        Map<String, String> map = new HashMap<>();
        map.put("url", oss.getUrl());
        map.put("fileName", oss.getOriginalName());
        map.put("ossId", oss.getOssId());
        return map;
    }

}
