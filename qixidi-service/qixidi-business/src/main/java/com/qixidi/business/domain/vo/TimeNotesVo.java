package com.qixidi.business.domain.vo;

import com.qixidi.business.domain.entity.TimeNotes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zi-wei
 * @create 2025/3/26 11:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeNotesVo {
    /**
     * 记录时间
     */
    private String recordTime;
    /**
     * 明细
     */
    private List<TimeNotes> list;
}
