package com.aurora.business.domain.label;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


/**
 * 标签信息对象 label_info
 *
 * @author ruoyi
 * @date 2022-07-09
 */
@Data
@TableName("label_info")
public class LabelInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 分组id
     */
    private Long labelGroupingId;
    /**
     * 名称
     */
    private String labelName;
    /**
     * 描述
     */
    private String labelDescribe;
    /**
     * 封面
     */
    private String labelCover;
    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;

}
