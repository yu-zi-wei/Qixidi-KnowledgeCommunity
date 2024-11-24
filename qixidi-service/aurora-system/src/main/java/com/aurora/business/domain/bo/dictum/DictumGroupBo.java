package com.aurora.business.domain.bo.dictum;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 名言分组业务对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("名言分组业务对象")
public class DictumGroupBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 分组名称
     */
    @ApiModelProperty(value = "分组名称", required = true)
    @NotBlank(message = "分组名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面", required = true)
    private String cover;

    /**
     * 简介
     */
    @ApiModelProperty(value = "简介", required = true)
    private String briefIntroduction;

    /**
     * 收录数
     */
    private Long employSum;

    /**
     * 状态（0：正常吗：已删除）
     */
    @ApiModelProperty(value = "状态（0：正常吗：已删除）", required = true)
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
