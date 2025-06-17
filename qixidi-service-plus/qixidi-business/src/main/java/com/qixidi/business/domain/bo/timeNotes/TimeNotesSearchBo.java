package com.qixidi.business.domain.bo.timeNotes;

import com.light.core.core.domain.PageQuery;
import lombok.Data;

/**
 * @author zi-wei
 * @create 2025/6/16 16:12
 */
@Data
public class TimeNotesSearchBo extends PageQuery {
    /**
     * 标题
     */
    private String title;
}
