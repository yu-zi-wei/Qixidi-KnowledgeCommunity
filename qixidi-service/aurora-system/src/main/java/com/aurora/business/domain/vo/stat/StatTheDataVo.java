package com.aurora.business.domain.vo.stat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 网站数据月统计视图对象 stat_the_data
 *
 * @author aurora
 * @date 2023-03-14
 */
@Data
@ApiModel("网站数据月统计视图对象")
@ExcelIgnoreUnannotated
public class StatTheDataVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private Long id;

    /**
     * 统计月份
     */
    @ExcelProperty(value = "统计月份")
    @ApiModelProperty("统计月份")
    private String statTime;

    /**
     * 用户总数
     */
    @ExcelProperty(value = "用户总数")
    @ApiModelProperty("用户总数")
    private Long userCount;

    /**
     * 在线用户总数
     */
    @ExcelProperty(value = "在线用户总数")
    @ApiModelProperty("在线用户总数")
    private Long onlineUserCount;

    /**
     * 文章总数
     */
    @ExcelProperty(value = "文章总数")
    @ApiModelProperty("文章总数")
    private Long articleCount;

    /**
     * 专栏总数
     */
    @ExcelProperty(value = "专栏总数")
    @ApiModelProperty("专栏总数")
    private Long specialCount;

    /**
     * 收藏夹总数
     */
    @ExcelProperty(value = "收藏夹总数")
    @ApiModelProperty("收藏夹总数")
    private Long favoritesCount;


    /**
     * 名言分类总数
     */
    private Long dictumGroupCount;

    /**
     * 名言专辑总数
     */
    private Long dictumAlbumCount;

    /**
     * 名言总数
     */
    private Long dictumCount;


}
