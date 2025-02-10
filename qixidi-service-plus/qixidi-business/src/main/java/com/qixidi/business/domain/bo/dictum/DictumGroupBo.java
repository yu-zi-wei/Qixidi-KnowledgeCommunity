package com.qixidi.business.domain.bo.dictum;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 名言分组业务对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DictumGroupBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String briefIntroduction;

    /**
     * 收录数
     */
    private Long employSum;

    /**
     * 状态（0：正常吗：已删除）
     */
    private Integer state;
    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 角色（1，普通用户，2，vip用户）
     */
    private String roleId;
    /**
     * 位置
     */
    private String location;

}
