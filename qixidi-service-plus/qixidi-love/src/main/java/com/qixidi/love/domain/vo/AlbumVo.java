/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:48
 * @version 1.0
 */
package com.qixidi.love.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;
import com.light.excel.convert.ExcelDictConvert;
import com.light.core.core.domain.BaseEntity;
import lombok.Data;


/**
 * 时光相册视图对象 album
 *
 * @author ziwei
 * @date 2022-11-20
 */
@Data
@ExcelIgnoreUnannotated
public class AlbumVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 图片链接
     */
    @ExcelProperty(value = "图片链接")
    private String img;

    private String imgId;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址")
    private String address;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;

    /**
     * 顺序
     */
    @ExcelProperty(value = "顺序")
    private Long order;

    /**
     * 状态（0，正常，1，已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=，正常，1，已删除")
    private Long state;

    /**
     * 用于查看大图
     */
    private Boolean states = false;


}

