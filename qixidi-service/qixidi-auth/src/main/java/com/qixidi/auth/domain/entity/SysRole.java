package com.qixidi.auth.domain.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.light.core.annotation.ExcelDictFormat;
import com.light.core.constant.UserConstants;
import com.light.core.core.domain.BaseEntity;
import com.light.excel.convert.ExcelDictConvert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色表 sys_role
 *
 * @author Lion Li
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@ExcelIgnoreUnannotated
public class SysRole extends BaseEntity {

    /**
     * 角色ID
     */
    @ExcelProperty(value = "角色序号")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String roleName;

    /**
     * 角色权限
     */
    @ExcelProperty(value = "角色权限")
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    private String roleKey;

    /**
     * 角色排序
     */
    @ExcelProperty(value = "角色排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty(value = "角色状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_common_status")
    private String status;

    /**
     * 角色状态多选筛选（in 查询，后台列表页）
     */
    @TableField(exist = false)
    private List<String> statuses;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    @TableField(exist = false)
    private boolean flag = false;

    /**
     * 所属用户ID（账号列表批量填充 roles 时按用户分组用，非表字段）
     */
    @TableField(exist = false)
    private Long userId;

    /**
     * 已分配权限码（sys_role_perm，随新增/编辑提交）
     */
    @TableField(exist = false)
    private List<String> perms;

    public SysRole(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.roleId);
    }
}
