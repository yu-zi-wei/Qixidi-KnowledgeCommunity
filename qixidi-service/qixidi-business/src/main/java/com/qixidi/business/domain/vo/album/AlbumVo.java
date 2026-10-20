package com.qixidi.business.domain.vo.album;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 相册图片
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AlbumVo {

    /**
     * id
     */
    private Long id;

    /**
     * 图片归属用户
     */
    private String uid;

    /**
     * 来源内容id（字符串避免 JS 精度丢失，前端拼跳转链接用）
     */
    private String sourceId;

    /**
     * 来源内容时间
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
