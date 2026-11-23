package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 用户浏览历史
 * <p>
 * 业务对象 b_browsing_history
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BrowsingHistoryBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 目标id
     */
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标标题
     */
    @NotBlank(message = "目标标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetTitle;

    /**
     * 目标uid
     */
    @NotBlank(message = "目标uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 目标类型（1：文章，2：小记，3：随笔）
     */
    @NotNull(message = "目标类型（1：文章，2：小记，3：随笔）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer targetType;

    /**
     * 行为类型（1：阅读，2：点赞，3：评论），可选，默认阅读
     */
    private Integer actionType;

}

