package com.qixidi.business.domain.entity.dictum;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 名言专辑对象 b_dictum_album
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_dictum_album")
public class DictumAlbum {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * uid
     */
    private String uid;
    /**
     * 专辑名称
     */
    private String name;
    /**
     * 封面
     */
    private String cover;
    /**
     * 简介
     */
    private String briefIntroduction;
    /**
     * 专辑状态（1：公开，2：私有，3：关注可看）
     */
    private Long albumState;

    /**
     * 收录总数
     */
    private Long employSum;

    /**
     * 推荐率
     */
    private Long recommendRate;

    /**
     * 点赞总数
     */
    private Long helpSum;
    /**
     * 关注总数
     */
    private Long followSum;
    /**
     * 状态（0：正常吗：已删除）
     */
    private Integer state;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;

}

