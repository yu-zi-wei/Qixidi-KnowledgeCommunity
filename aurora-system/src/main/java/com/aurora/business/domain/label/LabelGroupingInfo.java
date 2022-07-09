package com.aurora.business.domain.label;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


/**
 * 标签分组信息对象 label_grouping_info
 *
 * @author aurora
 * @date 2022-07-09
 */
@Data
@TableName("label_grouping_info")
public class LabelGroupingInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 分组名称
     */
    private String groupingName;
    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;
    /**
     * 描述
     */
    private String groupingDescribe;

}

