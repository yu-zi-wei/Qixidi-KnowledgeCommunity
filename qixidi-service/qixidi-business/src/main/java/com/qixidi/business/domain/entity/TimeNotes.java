package com.qixidi.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.light.core.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author zi-wei
 * @create 2025/3/26 10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_time_notes")
public class TimeNotes extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否存在内容
     */
    @TableField(exist = false)
    private Boolean isContent = false;

    /**
     * uid
     */
    private String uid;

    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordTime;
}
