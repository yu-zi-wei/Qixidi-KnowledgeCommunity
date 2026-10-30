package com.qixidi.system.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.system.domain.entity.SysRolePerm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色与权限码关联Mapper接口
 *
 * @author ziwei
 */
@Mapper
public interface SysRolePermMapper extends BaseMapperPlus<SysRolePermMapper, SysRolePerm, SysRolePerm> {

    /**
     * 查询用户的所有权限码（经 sys_user_role 关联）
     */
    List<String> selectPermsByUserId(Long userId);

    /**
     * 查询角色已分配的权限码
     */
    List<String> selectPermsByRoleId(Long roleId);

    /**
     * 删除角色的全部权限码分配
     */
    int deleteByRoleId(Long roleId);
}
