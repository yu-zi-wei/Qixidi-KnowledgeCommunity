package com.qixidi.business.domain.vo;

import com.qixidi.business.domain.entity.TimeNotes;
import lombok.Data;

/**
 * @author zi-wei
 * @create 2025/3/26 14:39
 */
@Data
public class TimeNotesInfoVo extends TimeNotes {
    /**
     * 是否为作者（0-是，1-不是）
     */
    private Integer isAuthor = 1;

    /**
     * 点赞数（b_fabulous_record type=2 实时聚合）
     */
    private Long likeTimes = 0L;

    /**
     * 当前登录用户是否已点赞（0-已赞，1-未赞；未登录恒为 1）
     */
    private Integer isLiked = 1;
}
