package com.aurora.business.domain.entity.fabulous;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


/**
 * 点赞对象 fabulous_record
 *
 * @author aurora
 * @date 2022-10-01
 */
@Data
@TableName("fabulous_record")
public class FabulousRecord {

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
     * 目标id
     */
    private Long targetId;
    /**
     * 类型id
     */
    private Long typeId;
    /**
     * 目标Uid
     */
    private String targetUid;
    /**
     * 点赞类型（1：文章，2：问答，3：回答，4：评论，5：回复）
     */
    private Integer type;
    /**
     * 状态（0：点赞有效，1：取消点赞）
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;
}
