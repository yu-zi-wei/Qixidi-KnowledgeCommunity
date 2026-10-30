package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.system.config.PermissionScanner;
import com.qixidi.system.domain.vo.PermGroupVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限码清单（来源：PermissionScanner 启动扫描，不落库）
 *
 * @author ziwei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/perm")
public class SysPermController {

    private final PermissionScanner permissionScanner;

    /**
     * 按模块分组的权限码清单（角色管理勾选树数据源）
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/list")
    public List<PermGroupVo> list() {
        return permissionScanner.getGroups();
    }
}
