package com.qixidi.business.domain.vo.article;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ArticleCensusVo {
    /**
     * 文章id
     */
    private Long id;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 专栏id
     */
    private Long specialId;

    /**
     * 点赞次数
     */
    private Long likeTimes;
    /**
     * 评论次数
     */
    private Long commentTimes;
    /**
     * 创建时间
     */
    private Date createTime;
}
