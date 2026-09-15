package com.qixidi.business.domain.entity.timeNotes;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 时光小记评论对象 b_time_notes_comment
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("b_time_notes_comment")
public class TimeNotesComment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 时光小记id
     */
    private Long timeNotesId;
    /**
     * 小记用户id
     */
    private String uid;
    /**
     * 父级评论id（一级评论 = 小记id）
     */
    private Long parentId;
    /**
     * 评论等级（1：一级，2：二级，3：三级及以下）
     */
    private Integer commentGrade;
    /**
     * 目标id（被回复的评论id或小记id）
     */
    private Long targetId;
    /**
     * 目标用户id
     */
    private String targetUid;
    /**
     * 评论人id
     */
    private String commentUid;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论类型（1：小记，2：评论）
     */
    private Integer type;
    /**
     * 评论状态（0：正常，1：已删除）
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
