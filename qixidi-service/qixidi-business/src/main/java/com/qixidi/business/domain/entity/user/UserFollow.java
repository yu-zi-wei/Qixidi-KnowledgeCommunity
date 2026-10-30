package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 用户关注对象 b_user_follow
 *
 * @author aurora
 * @date 2023-02-13
 */
@Data
@TableName("b_user_follow")
public class UserFollow {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;
    /**
     * 关注目标id
     */
    @ExcelProperty(value = "关注目标id")
    private String targetId;
    /**
     * 关注类型（1：用户，2：标签，3：活动，4：圈子）
     */
    @TableField("`type`")
    @ExcelProperty(value = "关注类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：用户，2：标签，3：活动，4：圈子")
    private Long type;
    /**
     * 状态（0：正常，1：已取消）
     */
    @TableField("`state`")
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：已取消")
    private Integer state;

    private Date createTime;

}
