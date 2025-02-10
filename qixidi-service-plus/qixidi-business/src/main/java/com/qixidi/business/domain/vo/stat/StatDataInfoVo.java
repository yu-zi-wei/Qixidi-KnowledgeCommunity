package com.qixidi.business.domain.vo.stat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 统计数据信息视图对象 b_stat_data_info
 *
 * @author aurora
 * @date 2023-03-14
 */
@Data
@ExcelIgnoreUnannotated
public class StatDataInfoVo {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 统计时间
     */
    @ExcelProperty(value = "统计时间")
    private String statTime;

    /**
     * 用户总数
     */
    @ExcelProperty(value = "用户总数")
    private Long userCount;

    /**
     * 在线用户总数
     */
    @ExcelProperty(value = "在线用户总数")
    private Long onlineUserCount;

    /**
     * 文章总数
     */
    @ExcelProperty(value = "文章总数")
    private Long articleCount;

    /**
     * 专栏总数
     */
    @ExcelProperty(value = "专栏总数")
    private Long specialCount;

    /**
     * 收藏夹总数
     */
    @ExcelProperty(value = "收藏夹总数")
    private Long favoritesCount;

    /**
     * 分类数
     */
    private Long labelGroupCount;

    /**
     * 标签数
     */
    private Long labelCount;

    /**
     * 名言分类总数
     */
    private Long dictumGroupCount;
    /**
     * 名言总数
     */
    private Long dictumCount;
    /**
     * 名言专辑总数
     */
    private Long dictumAlbumCount;

}
