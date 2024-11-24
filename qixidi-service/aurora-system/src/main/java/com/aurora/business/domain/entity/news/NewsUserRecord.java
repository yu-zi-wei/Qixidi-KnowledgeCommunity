package com.aurora.business.domain.entity.news;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ziwei
 * @date 2024年02月26日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_news_user_record")
public class NewsUserRecord {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 消息id
     */
    private Long newsId;

    /**
     * 消息目标id
     */
    private Long targetId;

    /**
     * 目标用户id
     */
    private String targetUid;

    /**
     * 类型
     */
    private int type;
    /**
     * 是否已读
     */
    private int beenRead = 0;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 内容
     */
    private String content;

}
