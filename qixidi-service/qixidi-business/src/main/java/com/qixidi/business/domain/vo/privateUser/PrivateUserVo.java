package com.qixidi.business.domain.vo.privateUser;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.privateUser.PrivateUser;


/**
 * 私信记录视图对象 b_private_user
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@ExcelIgnoreUnannotated
public class PrivateUserVo extends PrivateUser {

    private static final long serialVersionUID = 1L;

    /**
     * 未读消息条数
     */
    private Integer unreadCount = 0;

    /**
     * 目标用户名称
     */
    private String targetName;

    /**
     * 目标用户头像
     */
    private String targetAvatar;

    /**
     * 职业
     */
    private String targetOccupation;


}

