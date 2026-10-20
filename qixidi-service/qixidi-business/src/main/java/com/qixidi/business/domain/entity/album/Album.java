package com.qixidi.business.domain.entity.album;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.light.core.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 相册（图片来源于时光小记/文章内容中自动解析的图片）
 *
 * @author zi-wei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_album")
public class Album extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片归属用户
     */
    private String uid;

    /**
     * 来源内容id（时光小记id/文章id）
     */
    private Long sourceId;

    /**
     * 来源内容时间（小记record_time/文章发布时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate sourceTime;

    /**
     * 来源：1-时光小记，2-文章
     */
    private Integer source;

    /**
     * 图片地址
     */
    private String url;
}
