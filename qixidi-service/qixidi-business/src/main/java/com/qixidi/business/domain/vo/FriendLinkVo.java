package com.qixidi.business.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 友链
 *
 * @author ziwei
 * @date 2024年09月17日
 */
@Data
public class FriendLinkVo {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String linkName;
    /**
     * 简介
     */
    private String linkIntro;
    /**
     * 头像
     */
    private String linkAvatar;
    /**
     * 地址
     */
    private String linkUrl;
    /**
     * 加入时间
     */
    private Date createTime;
}
