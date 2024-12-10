package com.aurora.business.domain.entity.news;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 用户消息对象 b_news_user_info
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@Accessors(chain = true)
@TableName("b_news_user_info")
public class NewsUserInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 消息内容
     */
    private String newsContent;
    /**
     * 消息类型（1：评论消息，2：点赞消息，3：关注消息，4系统消息）
     */
    private Integer type;
    /**
     * 生产消息的目标者id
     */
    private String prnNewsWorksId;
    /**
     * 消息目标id
     */
    private String worksId;
    /**
     * 消息目标父级id
     */
    private String worksParentId;
    /**
     * 消息目标父及Uid
     */
    private String worksParentUid;
    /**
     * 消息目标类型（1：文章，2：评论，3：圈子，4：活动）
     */
    private Integer worksType;
    /**
     * 消息目标内容
     */
    private String worksContent;
    /**
     * 状态（0：正常，1：已删除）
     */
    @TableField("`state`")
    private Integer state;
    /**
     * 是否已读（0：未读，1：已读）
     */
    private Integer beenRead;
    /**
     * 发送者id
     */
    private String senderId;
    /**
     * 接收者id
     */
    private String recipientId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}

