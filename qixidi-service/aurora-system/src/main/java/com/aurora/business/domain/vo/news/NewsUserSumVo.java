package com.aurora.business.domain.vo.news;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 用户消息视图对象 b_news_user_info
 *
 * @author aurora
 * @date 2022-11-03
 */
@Data
@Accessors(chain = true)
public class NewsUserSumVo implements Serializable {
    /**
     * 消息类型（1：点赞消息，2：评论消息，3：关注消息）
     */
    private Integer type;
    /**
     * 消息类型信息
     */
    private String typeInfo;
    /**
     * 消息路由
     */
    private String route;
    /**
     * 消息总数
     */
    private Integer newsSum;

}
