package com.qixidi.business.domain.bo.fabulous;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 点赞业务对象 b_fabulous_record
 *
 * @author aurora
 * @date 2022-10-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class FabulousRecordBo extends BaseEntity {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 对应的作品id
     */
    @NotNull(message = "对应的作品id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long typeId;
    /**
     * 点赞对象的标签
     */
    private String labelId;
    /**
     * 目标id
     */
    @NotNull(message = "目标id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long targetId;

    @NotBlank(message = "目标标题", groups = {AddGroup.class, EditGroup.class})
    private String targetTitle;

    @NotBlank(message = "目标Uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String targetUid;

    /**
     * 点赞类型（1：文章，2：小记，3：文章评论，4：小记评论，5：随笔）
     */
    @NotNull(message = "点赞类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

}
