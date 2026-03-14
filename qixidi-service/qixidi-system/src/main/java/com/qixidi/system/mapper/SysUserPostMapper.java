package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与岗位关联表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysUserPostMapper extends BaseMapperPlus<SysUserPostMapper, SysUserPost, SysUserPost> {

}
