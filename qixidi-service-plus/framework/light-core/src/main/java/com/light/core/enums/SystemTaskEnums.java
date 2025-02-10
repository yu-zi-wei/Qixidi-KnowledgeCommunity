package com.light.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zi-wei
 * @create 2024/11/17 14:27
 */
@Getter
@AllArgsConstructor
public enum SystemTaskEnums {
    CALCULATING_ARTICLE_WEIGHTS(1, "计算文章权重任务"),
    SYNC_ARTICLE_CLICK_LIKE(2, "同步文章点赞任务"),
    SYNCING_QUOTES_DATA(3, "同步名言数据任务"),
    CALCULATING_HOT_DATA(4, "计算热门数据任务"),
    SYNCHRONIZE_b_label_infoRMATION_DATA(5, "同步标签信息数据任务"),
    SYNC_NUMBER_COLUMNS(6, "同步专栏文章数任务"),
    UPDATE_WEBSITE_DAILY_DATA(7, "更新网站每日数据任务"),
    UPDATE_WEBSITE_MONTHLY_DATA(8, "更新网站每月数据任务"),
    SYNCING_USER_SITE_DATA(9, "用户网站数据同步任务"),

    ;
    private Integer code;
    private String value;
}
