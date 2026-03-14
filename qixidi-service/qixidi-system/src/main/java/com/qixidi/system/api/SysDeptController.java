package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import com.light.core.constant.UserConstants;
import com.light.core.enums.BusinessType;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;

import com.qixidi.auth.domain.entity.SysDept;
import com.qixidi.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dept")
public class SysDeptController {

    private final ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list")
    public List<SysDept> list(SysDept dept) {
        return deptService.selectDeptList(dept);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public List<SysDept> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().equals(deptId)
                || ArrayUtil.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""));
        return depts;
    }

    /**
     * 根据部门编号获取详细信息
     */
    @SaCheckPermission("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public SysDept getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return deptService.selectDeptById(deptId);
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public List<Tree<Long>> treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return deptService.buildDeptTreeSelect(depts);
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public Map<String, Object> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @SaCheckPermission("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public void add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            throw new ServiceException("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        deptService.insertDept(dept);
    }

    /**
     * 修改部门
     */
    @SaCheckPermission("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public void edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            throw new ServiceException("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            throw new ServiceException("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            throw new ServiceException("该部门包含未停用的子部门！");
        }
        deptService.updateDept(dept);
    }

    /**
     * 删除部门
     */
    @SaCheckPermission("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public void remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            throw new ServiceException("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            throw new ServiceException("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        deptService.deleteDeptById(deptId);
    }
}
