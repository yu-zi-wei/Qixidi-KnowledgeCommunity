package com.qixidi.system.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 权限码分组视图对象（角色管理勾选树数据源）
 *
 * @author ziwei
 */
@Data
@NoArgsConstructor
public class PermGroupVo {

    /**
     * 模块名（权限码首段，如 article/role/monitor）
     */
    private String module;

    /**
     * 该模块下的全部权限码（已排序）
     */
    private List<String> perms;

    public PermGroupVo(String module, List<String> perms) {
        this.module = module;
        this.perms = perms;
    }
}
