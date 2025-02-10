package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysOss;
import com.qixidi.system.domain.vo.SysOssVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOssMapper extends BaseMapperPlus<SysOssMapper, SysOss, SysOssVo> {
}
