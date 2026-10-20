package com.qixidi.business.domain.bo.album;

import com.light.core.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 相册查询入参
 *
 * @author zi-wei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumSearchBo extends PageQuery {

    /**
     * 可选：按用户筛选
     */
    private String uid;

    /**
     * 可选：时间筛选，yyyy-MM 按月 / yyyy 按年（目录点远月时只拉该范围，省流量）
     */
    private String month;
}
