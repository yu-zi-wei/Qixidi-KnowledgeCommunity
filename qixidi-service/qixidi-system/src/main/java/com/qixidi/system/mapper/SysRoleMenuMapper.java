package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与菜单关联表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapperPlus<SysRoleMenuMapper, SysRoleMenu, SysRoleMenu> {

}
