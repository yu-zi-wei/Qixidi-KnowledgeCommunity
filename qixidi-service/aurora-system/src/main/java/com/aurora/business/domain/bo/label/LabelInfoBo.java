package com.aurora.business.domain.bo.label;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 标签信息业务对象 b_label_info
 *
 * @author aurora
 * @date 2022-08-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * uid
     */
    private String uid;
    /**
     * 类型
     */
    private Integer type;

    /**
     * 分组id
     */
    @NotNull(message = "分组id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long labelGroupingId;
    /**
     * 分组名称
     */
    private String groupingName;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String labelName;

    /**
     * 描述
     */
    private String labelDescribe;

    /**
     * 封面
     */
//    @NotBlank(message = "封面不能为空", groups = {AddGroup.class, EditGroup.class})
    private String labelCover;

    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;
    /**
     * 关注数
     */
    private Integer followNumber;
    /**
     * 文章数
     */
    private Integer articleNumber;

}

