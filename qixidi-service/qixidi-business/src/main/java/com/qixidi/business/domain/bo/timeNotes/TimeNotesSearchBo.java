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

    /**
     * uid
     */
    private Long uid;

    /**
     * 作者昵称模糊搜索（后台列表页：用户域查 uuid 集合后 in）
     */
    private String nickname;
}
