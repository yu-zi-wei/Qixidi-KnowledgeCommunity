package com.qixidi.business.domain.entity.count;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户网站信息统计表
 */
@Data
@ExcelIgnoreUnannotated
@TableName("b_count_user_website")
@Accessors(chain = true)
@NoArgsConstructor
public class CountUserWebsiteEntity implements Serializable {
    /**
     * 用户id
     */
    private String uuid;
    /**
     * 点赞数
     */
    private int fabulousCount;
    /**
     * 被点赞数
     */
    private int fansFabulousCount;
    /**
     * 收藏数
     */
    private int collectionCount;
    /**
     * 关注数
     */
    private int followCount;
    /**
     * 被关注数
     */
    private int fansFollowCount;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 被评论数
     */
    private int fansCommentCount;
    /**
     * 文章数
     */
    private int articleCount;
    /**
     * 专栏数
     */
    private int specialColumnCount;
    /**
     * 被关注专栏数
     */
    private int fansSpecialColumn;
    /**
     * 圈子数
     */
    private int circleCount;
    /**
     * 专辑数
     */
    private int albumCount;
    /**
     * 名言数
     */
    private int dictumCount;
    /**
     * 时光小记数
     */
    private int timeNotesCount;

    public void fillCountUserWebsite(CountUserWebsiteEntity countUserWebsiteEntity) {
        this.fabulousCount = countUserWebsiteEntity.getFabulousCount();
        this.fansFabulousCount = countUserWebsiteEntity.getFansFabulousCount();
        this.collectionCount = countUserWebsiteEntity.getCollectionCount();
        this.followCount = countUserWebsiteEntity.getFollowCount();
        this.fansFollowCount = countUserWebsiteEntity.getFansFollowCount();
        this.commentCount = countUserWebsiteEntity.getCommentCount();
        this.fansCommentCount = countUserWebsiteEntity.getFansCommentCount();
        this.articleCount = countUserWebsiteEntity.getArticleCount();
        this.specialColumnCount = countUserWebsiteEntity.getSpecialColumnCount();
        this.fansSpecialColumn = countUserWebsiteEntity.getFansSpecialColumn();
        this.circleCount = countUserWebsiteEntity.getCircleCount();
        this.albumCount = countUserWebsiteEntity.getAlbumCount();
        this.dictumCount = countUserWebsiteEntity.getDictumCount();
        this.timeNotesCount = countUserWebsiteEntity.getTimeNotesCount();
    }
}
