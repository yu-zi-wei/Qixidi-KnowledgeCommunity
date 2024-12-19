package com.aurora.business.domain.entity.dictum;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 名言信息对象 b_dictum_info
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_dictum_info")
public class DictumInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 内容
     */
    private String content;
    /**
     * 内容
     */
    private String contentMd;
    /**
     * 分类id
     */
    private Long groupId;

    /**
     * 专辑id
     */
    private Long albumId;

    /**
     * 标签（多个以逗号隔开）
     */
    private String label;

    private String author;

    private String worksName;

    /**
     * 图片（多个以逗号隔开）
     */
    private String picture;
    /**
     * 名言状态（1：公开，1：私有，3：关注可看）
     */
    private Integer dictumState;
    /**
     * 状态（0，正常，2：以删除）
     */
    private Integer state;

    private Date createTime;

    private Date updateTime;

}

