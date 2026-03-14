package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 用户关注业务对象 b_user_follow
 *
 * @author aurora
 * @date 2023-02-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFollowBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 关注目标id
     */
    @NotBlank(message = "关注目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetId;

    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    @NotNull(message = "关注类型（1：用户，2：标签，3：活动，4：圈子）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 状态（0：正常，1：已取消）
     */
    private Integer state;


}

