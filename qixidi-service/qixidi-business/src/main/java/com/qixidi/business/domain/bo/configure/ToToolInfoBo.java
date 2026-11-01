package com.qixidi.business.domain.bo.configure;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;


/**
 * 工具信息业务对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToToolInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 工具名称
     */
    @NotBlank(message = "工具名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String toolName;

    /**
     * 描述
     */
    private String describe;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否为父级（1；父级，2：子级）
     */
    @NotNull(message = "是否为父级（1；父级，2：子级）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer isParent;

    /**
     * 工具地址（仅子级工具必填，目录不需要——Service 层按 isParent 校验）
     */
    private String toolUrl;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long order;

    /**
     * 类型
     */
    private Long type;

    /**
     * 状态（0：正常，1：已失效）
     */
    private Integer state;

    /**
     * 状态多选筛选（in 查询，后台列表页）
     */
    private List<Integer> states;

    /**
     * 层级多选筛选（in 查询，后台列表页）
     */
    private List<Integer> isParents;

    /**
     * 所属目录多选筛选（in 查询，后台工具 tab）
     */
    private List<Long> parentIds;
    /**
     * 创建人
     */
    private String createBy;


}

