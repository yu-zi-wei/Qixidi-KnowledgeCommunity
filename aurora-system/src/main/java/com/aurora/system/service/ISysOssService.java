package com.aurora.system.service;

import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.system.domain.SysOss;
import com.aurora.system.domain.bo.SysOssBo;
import com.aurora.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService {

    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    SysOss getById(Long ossId);

    SysOss upload(MultipartFile file);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
