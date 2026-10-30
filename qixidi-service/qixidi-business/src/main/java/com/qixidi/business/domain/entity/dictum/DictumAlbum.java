package com.qixidi.business.domain.entity.dictum;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 名言专辑对象 b_dictum_album
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_dictum_album")
public class DictumAlbum {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * uid
     */
    @ExcelProperty(value = "uid")
    private String uid;
    /**
     * 专辑名称
     */
    @ExcelProperty(value = "专辑名称")
    private String name;
    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String cover;
    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String briefIntroduction;
    /**
     * 专辑状态（1：公开，2：私有，3：关注可看）
     */
    @ExcelProperty(value = "专辑状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：公开，2：私有，3：关注可看")
    private Long albumState;

    /**
     * 推荐率
     */
    private Long recommendRate;

    /**
     * 状态（0：正常吗：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常吗：已删除")
    private Integer state;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;

}

