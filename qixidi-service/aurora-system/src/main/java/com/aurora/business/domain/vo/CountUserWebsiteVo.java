package com.aurora.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@ApiModel("用户网站信息统计VO")
@ExcelIgnoreUnannotated
@Accessors(chain = true)
public class CountUserWebsiteVo {

    private String uuid;

    @ApiModelProperty("点赞数")
    private int fabulousCount;

    @ApiModelProperty("被点赞数")
    private int fansFabulousCount;

    @ApiModelProperty("收藏数")
    private int collectionCount;

    @ApiModelProperty("关注数")
    private int followCount;

    @ApiModelProperty("被关注数")
    private int fansFollowCount;

    @ApiModelProperty("评论数")
    private int commentCount;

    @ApiModelProperty("被评论数")
    private int fansCommentCount;

    @ApiModelProperty("文章数")
    private int articleCount;

    @ApiModelProperty("专栏数")
    private int specialColumnCount;

    @ApiModelProperty("被关注专栏数")
    private int fansSpecialColumn;

    @ApiModelProperty("圈子数")
    private int circleCount;

    @ApiModelProperty("专辑数")
    private int albumCount;

    @ApiModelProperty("名言数")
    private int dictumCount;

    private Date updateTime = new Date();
}
