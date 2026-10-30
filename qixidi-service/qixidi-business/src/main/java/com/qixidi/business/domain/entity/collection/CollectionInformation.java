package com.qixidi.business.domain.entity.collection;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 收藏夹信息对象 b_collection_information
 *
 * @author aurora
 * @date 2022-09-29
 */
@Data
@TableName("b_collection_information")
public class CollectionInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏夹id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "收藏夹id")
    private Long id;
    /**
     * 收藏夹名称
     */
    @ExcelProperty(value = "收藏夹名称")
    private String collectionName;
    /**
     * 收藏夹简介
     */
    @ExcelProperty(value = "收藏夹简介")
    private String collectionIntroduce;
    /**
     * 收藏夹状态（0：正常，1：未启用）
     */
    @ExcelDictFormat(readConverterExp = "0=：正常，1：未启用")
    private Long state;
    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;

    @ExcelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改者id
     */
    @ExcelProperty(value = "修改者id")
    private String updateId;

    private Date updateTime;
}

