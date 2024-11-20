package com.aurora.business.domain.vo.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ArticleCensusVo {
    /**
     * 文章id
     */
    @ApiModelProperty("文章id")
    private Long id;

    /**
     * 作者id
     */
    @ApiModelProperty("作者id")
    private String userId;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String articleTitle;

    /**
     * 专栏id
     */
    @ApiModelProperty("专栏id")
    private Long specialId;


    @ApiModelProperty("点赞次数")
    private Long likeTimes;

    @ApiModelProperty("评论次数")
    private Long commentTimes;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
