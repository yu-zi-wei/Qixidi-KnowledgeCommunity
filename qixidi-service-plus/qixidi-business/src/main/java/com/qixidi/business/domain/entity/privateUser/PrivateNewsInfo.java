package com.qixidi.business.domain.entity.privateUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 私信记录对象 b_private_news_info
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@TableName("b_private_news_info")
public class PrivateNewsInfo {

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
     * 消息内容
     */
    private String newsComment;
    /**
     * 目标uid
     */
    private String replyTargetUid;

    /**
     * 标识（与上一条消息时间间隔0：20分钟以内，1：超过20分钟）
     */
    private Integer timeMark;

    private Date createTime;

    /**
     * 是否已读（1：未读，2：已读）
     */
    private Integer beenRead;

}
