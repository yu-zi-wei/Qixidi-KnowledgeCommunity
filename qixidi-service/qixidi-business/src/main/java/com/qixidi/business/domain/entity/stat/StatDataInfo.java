package com.qixidi.business.domain.entity.stat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统计数据信息对象 b_stat_data_info
 *
 * @author aurora
 * @date 2023-03-14
 */
@Data
@Accessors(chain = true)
@TableName("b_stat_data_info")
public class StatDataInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 统计时间
     */
    private String statTime;
    /**
     * 用户总数
     */
    private Long userCount;
    /**
     * 在线用户总数
     */
    private Long onlineUserCount;
    /**
     * 文章总数
     */
    private Long articleCount;
    /**
     * 专栏总数
     */
    private Long specialCount;
    /**
     * 收藏夹总数
     */
    private Long favoritesCount;
    /**
     * 名言分类总数
     */
    private Long dictumGroupCount;
    /**
     * 名言总数
     */
    private Long dictumCount;
    /**
     * 名言专辑总数
     */
    private Long dictumAlbumCount;

    /**
     * 时光小记总数
     */
    private Long timeNotesCount;

}

