package com.qixidi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.minio.domain.dto.MinioDto;
import com.light.minio.service.MinioService;
import com.light.oss.service.OssService;
import com.qixidi.system.domain.bo.SysOssBo;
import com.qixidi.system.domain.entity.SysOss;
import com.qixidi.system.domain.vo.SysOssVo;
import com.qixidi.system.mapper.SysOssMapper;
import com.qixidi.system.service.ISysOssService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysOssServiceImpl implements ISysOssService {

    private final SysOssMapper baseMapper;
    private final OssService ossService;
    private final MinioService minioService;

    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(SysOss::getCreateTime);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        return lqw;
    }

    @Override
    public SysOss getById(Long ossId) {
        return baseMapper.selectById(ossId);
    }

    @Override
    public SysOss upload(MultipartFile file) {
        //Minio 上传
        MinioDto upload = minioService.upload(file);
        log.info("Minio_url:" + upload.getUrl());
        SysOss sysOss = BeanUtil.copyProperties(upload, SysOss.class);

        //oss上传（不使用）
//        OssDto ossDto = ossService.upload(file);
//        log.info("ossDto_url:" + ossDto.getUrl());
//        SysOss sysOss = BeanUtil.copyProperties(ossDto, SysOss.class);
        // 保存文件信息
        baseMapper.insert(sysOss);
        return sysOss;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
