package com.aurora.business.domain.bo.dictum;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 名言评论业务对象
 *
 * @author aurora
 * @date 2022-11-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DictumCommentBo extends BaseEntity {

    /**
     * 名言id
     */
    @NotNull(message = "名言id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long dictumId;

    /**
     * 目标内容
     */
    private String worksContent;

    /**
     * 名言用户id
     */
    @NotBlank(message = "名言用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String uid;

    /**
     * 父级评论id
     */
    @NotNull(message = "父级评论id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long parentId;

    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    @NotNull(message = "评论等级不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer commentGrade;

    /**
     * 目标id
     */
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    /**
     * 目标用户id
     */
    @NotBlank(message = "目标用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 评论人id
     */
    private String commentUid;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 评论类型（1：名言，2：评论）
     */
    @NotNull(message = "评论类型（1：名言，2：评论）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 评论状态（0：正常，1：已删除）
     */
    private Integer status;

}
