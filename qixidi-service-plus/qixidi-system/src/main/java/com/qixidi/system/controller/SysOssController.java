package com.qixidi.system.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.qixidi.auth.annotation.Log;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.core.utils.file.FileUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.system.domain.bo.SysOssBo;
import com.qixidi.system.domain.entity.SysOss;
import com.qixidi.system.domain.vo.SysOssVo;
import com.qixidi.system.service.ISysOssService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss")
public class SysOssController extends BaseController {

    private final ISysOssService iSysOssService;

    /**
     * 查询OSS对象存储列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssVo> list(@Validated(QueryGroup.class) SysOssBo bo, PageQuery pageQuery) {
        return iSysOssService.queryPageList(bo, pageQuery);
    }

    /**
     * 上传OSS对象存储
     */
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestPart("file") MultipartFile file) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }
        SysOss oss = iSysOssService.upload(file);
        Map<String, Object> mapObj = new HashMap<>();
        mapObj.put("errorCode", 0);
        Map<String, String> map = new HashMap<>();
        map.put("url", oss.getUrl());
        map.put("src", oss.getUrl());
        map.put("href", oss.getUrl());
        map.put("fileName", oss.getOriginalName());
        map.put("alt", oss.getOriginalName());
        map.put("ossId", oss.getOssId().toString());
        //封装最终返回参数
        mapObj.put("data", map);
        return mapObj;
    }

    /**
     * 下载OSS对象存储
     *
     * @param ossId
     * @param response
     * @throws IOException
     */
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    public void download(@PathVariable Long ossId, HttpServletResponse response) throws IOException {
        SysOss sysOss = iSysOssService.getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        response.reset();
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        long data;
        try {
            data = HttpUtil.download(sysOss.getUrl(), response.getOutputStream(), false);
        } catch (HttpException e) {
            if (e.getMessage().contains("403")) {
                throw new ServiceException("无读取权限, 请在对应的OSS开启'公有读'权限!");
            } else {
                throw new ServiceException(e.getMessage());
            }
        }
        response.setContentLength(Convert.toInt(data));
    }

    /**
     * 删除OSS对象存储
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    public R<Void> remove(@PathVariable Long[] ossIds) {
        return toAjax(iSysOssService.deleteWithValidByIds(Arrays.asList(ossIds), true) ? 1 : 0);
    }

}
