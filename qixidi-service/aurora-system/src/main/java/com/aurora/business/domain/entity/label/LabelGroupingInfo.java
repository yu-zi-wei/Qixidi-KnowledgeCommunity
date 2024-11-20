package com.aurora.business.domain.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 标签分组信息对象 label_grouping_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@TableName("label_grouping_info")
public class LabelGroupingInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 分组名称
     */
    private String groupingName;

    /**
     * 图标
     */
    private String icon;
    /**
     * 状态（0：正常，1：已删除）
     */
    private Integer state;
    /**
     * 描述
     */
    private String groupingDescribe;

    private Long createBy;

    private Date createTime;
}

