package com.qixidi.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色与权限码关联 sys_role_perm
 *
 * @author ziwei
 */
@Data
@NoArgsConstructor
@TableName("sys_role_perm")
public class SysRolePerm {

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限码（模块:实体:动作）
     */
    private String perm;

    public SysRolePerm(Long roleId, String perm) {
        this.roleId = roleId;
        this.perm = perm;
    }
}
