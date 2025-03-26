package com.qixidi.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 用户反馈对象 Feedback
 *
 * @author aurora
 * @date 2023-04-17
 */
@Data
@TableName("b_feedback")
public class Feedback {

    private static final long serialVersionUID = 1L;

    /**
     *id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 反馈内容
     */
    private String FeedbackContent;
    /**
     * 反馈时间
     */
    private Date createTime;

    /**
     * 反馈标题
     */
    private String FeedbackTitle;

    /**
     * 状态（1-待处理，2-进行中，3-已完成，4-已关闭）
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

}

