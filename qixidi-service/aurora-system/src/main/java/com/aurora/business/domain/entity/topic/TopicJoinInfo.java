package com.aurora.business.domain.entity.topic;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


/**
 * 话题参与信息对象 topic_join_info
 *
 * @author aurora
 * @date 2023-03-09
 */
@Data
@TableName("topic_join_info")
public class TopicJoinInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 话题id
     */
    private Long topicId;
    /**
     * 内容
     */
    private String content;
    /**
     * 附件
     */
    private String enclosure;
    /**
     * 附件类型
     */
    private String enclosureType;
    /**
     * 状态（0：正常，1：已删除）
     */
    @TableField("`state`")
    private Long state;

}
