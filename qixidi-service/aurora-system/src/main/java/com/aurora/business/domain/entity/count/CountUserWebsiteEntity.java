package com.aurora.business.domain.entity.count;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ApiModel("用户网站信息统计表")
@ExcelIgnoreUnannotated
@TableName("b_count_user_website")
@Accessors(chain = true)
public class CountUserWebsiteEntity implements Serializable {

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
}
