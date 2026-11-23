package com.qixidi.business.domain.bo.news;

import com.light.core.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点赞消息列表查询入参（newsType 可选：2=文章点赞、8=小记点赞，缺省=全部混排）
 *
 * @author zi-wei
 * @date 2026-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsFabulousQueryBo extends PageQuery {

    /**
     * 消息类型过滤（2：文章点赞，8：小记点赞；不传查全部）
     */
    private Integer newsType;
}
