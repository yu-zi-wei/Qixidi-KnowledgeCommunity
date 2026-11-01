package com.qixidi.system.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限码条目（角色管理勾选项）
 *
 * @author ziwei
 */
@Data
@NoArgsConstructor
public class PermItemVo {

    /**
     * 权限码原值（勾选/提交/鉴权用，如 article:comment:remove）
     */
    private String code;

    /**
     * 中文展示名（PermissionScanner 按对象前缀 + 动作词根推导，如 文章评论-删除；未登记词根回退英文原段）
     */
    private String label;

    public PermItemVo(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
