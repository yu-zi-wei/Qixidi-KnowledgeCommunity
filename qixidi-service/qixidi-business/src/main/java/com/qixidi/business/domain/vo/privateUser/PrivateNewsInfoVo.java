package com.qixidi.business.domain.vo.privateUser;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.core.domain.BaseEntity;
import lombok.Data;


/**
 * 私信记录视图对象 b_private_news_info
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@ExcelIgnoreUnannotated
public class PrivateNewsInfoVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;

    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    private String newsComment;

    /**
     * 目标uid
     */
    @ExcelProperty(value = "目标uid")
    private String replyTargetUid;

    /**
     * 标识（与上一条消息时间间隔0：20分钟以内，1：超过20分钟）
     */
    private Integer timeMark;


    /**
     * 是否已读（1：未读，2：已读）
     */
    private Integer beenRead;

}

