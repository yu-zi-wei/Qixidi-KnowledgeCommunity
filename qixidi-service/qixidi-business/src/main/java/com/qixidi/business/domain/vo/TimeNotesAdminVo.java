package com.qixidi.business.domain.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 小记后台管理视图对象（后台列表专用：fill 作者昵称/头像）
 *
 * @author ziwei
 */
@Data
public class TimeNotesAdminVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容（markdown）
     */
    private String content;

    /**
     * 作者 uid
     */
    private String uid;

    /**
     * 作者昵称（fill）
     */
    private String nickname;

    /**
     * 作者头像（fill）
     */
    private String avatar;

    /**
     * 记录时间
     */
    private LocalDate recordTime;
}
