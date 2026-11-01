package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色关联表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

}
