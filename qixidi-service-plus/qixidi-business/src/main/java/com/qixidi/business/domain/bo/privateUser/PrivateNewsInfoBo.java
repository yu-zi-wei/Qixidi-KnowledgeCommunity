package com.qixidi.business.domain.bo.privateUser;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 私信用户业务对象 b_private_news_info
 *
 * @author aurora
 * @date 2023-03-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateNewsInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsComment;

    /**
     * 目标uid
     */
    @NotBlank(message = "目标uid不能为空", groups = {AddGroup.class, EditGroup.class})
    private String replyTargetUid;

    /**
     * 是否已读（1：未读，2：已读）
     */
    private Integer beenRead;

}

