package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户关注视图对象 user_follow
 *
 * @author aurora
 * @date 2023-02-13
 */
@Data
@ApiModel("用户关注视图对象")
@ExcelIgnoreUnannotated
public class UserFollowVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String uid;

    /**
     * 关注目标id
     */
    @ExcelProperty(value = "关注目标id")
    @ApiModelProperty("关注目标id")
    private String targetId;

    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    @ExcelProperty(value = "关注类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：用户，2：标签，3：活动，4：圈子")
    @ApiModelProperty("关注类型（1：用户，2：标签，3：活动，4：圈子）")
    private Long type;

    /**
     * 状态（0：正常，1：已取消）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已取消")
    @ApiModelProperty("状态（0：正常，1：已取消）")
    private Integer state;

    /**
     * 总数
     */
    private Integer sum;


}

