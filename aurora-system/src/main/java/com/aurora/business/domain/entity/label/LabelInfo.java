package com.aurora.business.domain.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 标签信息对象 label_info
 *
 * @author aurora
 * @date 2022-08-16
 */
@Data
@TableName("label_info")
public class LabelInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    private Long createBy;

    private Date createTime;
}

