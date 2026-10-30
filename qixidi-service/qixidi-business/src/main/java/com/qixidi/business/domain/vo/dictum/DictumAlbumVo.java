package com.qixidi.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.dictum.DictumAlbum;


/**
 * 名言专辑视图对象 b_dictum_album
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class DictumAlbumVo extends DictumAlbum {

    private static final long serialVersionUID = 1L;

    /**
     * 收录总数
     */
    private Long employSum;

    /**
     * 点赞总数
     */
    @ExcelProperty(value = "点赞总数")
    private Long helpSum;

    /**
     * 关注总数
     */
    @ExcelProperty(value = "关注总数")
    private Long followSum;

}
