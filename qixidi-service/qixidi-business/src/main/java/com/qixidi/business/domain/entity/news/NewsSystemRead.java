package com.qixidi.business.domain.entity.news;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 系统消息已读记录（Redis USER_SYSTEM_MESSAGES:{uid} 的落库兜底，Redis 丢失时回灌数据源）
 *
 * @author ziwei
 * @date 2026年11月01日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("b_news_system_read")
public class NewsSystemRead {
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
     * 系统消息id
     */
    private Long newsId;

    /**
     * 已读时间
     */
    private Date createTime;

}
