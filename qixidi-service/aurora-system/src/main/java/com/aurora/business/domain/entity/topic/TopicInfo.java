package com.aurora.business.domain.entity.topic;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 话题信息对象 topic_info
 *
 * @author aurora
 * @date 2023-03-09
 */
@Data
@TableName("topic_info")
public class TopicInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 封面
     */
    private String cover;
    /**
     * 话题名称
     */
    private String topicName;
    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 顺序
     */
    @TableField("`order`")
    private Long order;
    /**
     * 状态（0：开启，1：已结束）
     */
    @TableField("`state`")
    private Long state;
    /**
     * 参与人数
     */
    private Long number;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    private Date createTime;

    private String createBy;

}
