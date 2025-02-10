package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysRoleDept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与部门关联表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysRoleDeptMapper extends BaseMapperPlus<SysRoleDeptMapper, SysRoleDept, SysRoleDept> {

}
