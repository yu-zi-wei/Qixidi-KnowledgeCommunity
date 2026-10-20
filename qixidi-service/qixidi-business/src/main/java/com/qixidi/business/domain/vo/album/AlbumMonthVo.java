package com.qixidi.business.domain.vo.album;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 相册月份统计（目录用）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumMonthVo {

    /**
     * 月份 yyyy-MM
     */
    private String month;

    /**
     * 图片张数
     */
    private Long count;
}
