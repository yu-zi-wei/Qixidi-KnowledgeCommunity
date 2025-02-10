package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysOssConfig;
import com.qixidi.system.domain.vo.SysOssConfigVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对象存储配置Mapper接口
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
@Mapper
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfigMapper, SysOssConfig, SysOssConfigVo> {

}
