package com.aurora.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 名言专辑视图对象 dictum_album
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ApiModel("名言专辑视图对象")
@ExcelIgnoreUnannotated
public class DictumAlbumVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * uid
     */
    @ExcelProperty(value = "uid")
    @ApiModelProperty("uid")
    private String uid;

    /**
     * 专辑名称
     */
    @ExcelProperty(value = "专辑名称")
    @ApiModelProperty("专辑名称")
    private String name;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    @ApiModelProperty("封面")
    private String cover;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    @ApiModelProperty("简介")
    private String briefIntroduction;

    /**
     * 专辑状态（1：公开，2：私有，3：关注可看）
     */
    @ExcelProperty(value = "专辑状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：公开，2：私有，3：关注可看")
    @ApiModelProperty("专辑状态（1：公开，2：私有，3：关注可看）")
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
    @ExcelProperty(value = "点赞总数")
    @ApiModelProperty("点赞总数")
    private Long helpSum;

    /**
     * 关注总数
     */
    @ExcelProperty(value = "关注总数")
    @ApiModelProperty("关注总数")
    private Long followSum;

    /**
     * 状态（0：正常吗：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常吗：已删除")
    @ApiModelProperty("状态（0：正常吗：已删除）")
    private Integer state;

    private Date createTime;
    private Date updateTime;

}
