package com.qixidi.business.domain.bo.news;

import com.light.core.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户消息记录查询对象 b_news_user_record（后台管理）
 *
 * @author ziwei
 * @date 2026年11月01日
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsUserRecordBo extends PageQuery {

    /**
     * 消息类型多选（1：评论，2：点赞，3：关注，6：随笔评论，7：小记评论）
     */
    private List<Integer> types;

    /**
     * 用户名关键词（匹配发送者或接收者的昵称/用户名）
     */
    private String username;

}
