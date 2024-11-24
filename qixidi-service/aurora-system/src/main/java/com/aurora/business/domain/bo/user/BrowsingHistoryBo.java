package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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
@ApiModel("用户浏览历史业务对象")
public class BrowsingHistoryBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 目标id
     */
    @ApiModelProperty(value = "目标id", required = true)
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标标题
     */
    @ApiModelProperty(value = "目标标题", required = true)
    @NotBlank(message = "目标标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetTitle;

    /**
     * 目标uid
     */
    @ApiModelProperty(value = "目标uid", required = true)
    @NotBlank(message = "目标uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 目标类型（1：文章，2：帖子）
     */
    @ApiModelProperty(value = "目标类型（1：文章，2：帖子）", required = true)
    @NotNull(message = "目标类型（1：文章，2：帖子）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer targetType;


}

