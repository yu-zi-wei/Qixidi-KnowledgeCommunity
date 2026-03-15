package com.qixidi.business.domain.bo.dictum;

import com.light.core.core.domain.PageQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zi-wei
 * @create 2026/3/14 19:58
 */
@Data
@NoArgsConstructor
public class DictumInfoSearchBo extends PageQuery {
    /**
     * 专辑名称
     */
    private String albumName;
}
