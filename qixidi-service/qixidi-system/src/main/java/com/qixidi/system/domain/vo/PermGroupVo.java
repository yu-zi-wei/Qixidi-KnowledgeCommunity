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
     * 模块中文标题（如 文章管理；未登记词根回退 module 原文）
     */
    private String moduleLabel;

    /**
     * 该模块下的全部权限码条目（按 code 排序）
     */
    private List<PermItemVo> perms;

    public PermGroupVo(String module, String moduleLabel, List<PermItemVo> perms) {
        this.module = module;
        this.moduleLabel = moduleLabel;
        this.perms = perms;
    }
}
