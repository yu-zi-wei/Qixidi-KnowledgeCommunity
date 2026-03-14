package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 用户浏览历史
 * <p>
 * 对象 b_browsing_history
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_browsing_history")
public class BrowsingHistory {

    private static final long serialVersionUID = 1L;

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
     * 目标id
     */
    private Long targetId;
    /**
     * 状态
     */
    private Long state;
    /**
     * 目标标题
     */
    private String targetTitle;
    /**
     * 目标uid
     */
    private String targetUid;
    /**
     * 目标类型（1：文章，2：帖子）
     */
    private Integer targetType;

    private Date createTime;

    private Date updateTime;

}
