package com.aurora.business.domain.vo.fabulous;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 点赞视图对象 b_fabulous_record
 *
 * @author aurora
 * @date 2022-10-01
 */
@Data
@ExcelIgnoreUnannotated
public class FabulousRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;
    /**
     * 用户名称
     */
    @ExcelProperty(value = "用户名称")
    private String nickname;

    /**
     * 对应的作品或评论的id
     */
    @ExcelProperty(value = "对应的作品或评论的id")
    private Long typeId;

    /**
     * 点赞类型（1：文章，2：问答，3：回答，4：评论，5：回复）
     */
    @ExcelProperty(value = "点赞类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2：问答，3：回答，4：评论，5：回复")
    private Integer type;

    /**
     * 状态（0：点赞有效，1：取消点赞）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：点赞有效，1：取消点赞")
    private Integer state;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = ExcelDictConvert.class)
    private Date createTime;

}
