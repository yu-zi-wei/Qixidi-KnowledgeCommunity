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
}
