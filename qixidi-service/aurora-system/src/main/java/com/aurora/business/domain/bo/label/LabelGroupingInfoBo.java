package com.aurora.business.domain.bo.label;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 标签分组信息业务对象 b_label_grouping_info
 *
 * @author aurora
 * @date 2022-08-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LabelGroupingInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String groupingName;

    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String groupingDescribe;


}

