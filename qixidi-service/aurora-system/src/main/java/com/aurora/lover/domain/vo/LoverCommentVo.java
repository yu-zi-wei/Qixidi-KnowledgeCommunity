/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:27
 * @version 1.0
 */
package com.aurora.lover.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.core.domain.BaseEntity;
import lombok.Data;


/**
 * 爱情祝福语视图对象 lover_comment
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Data
@ExcelIgnoreUnannotated
public class LoverCommentVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    private String name;

    /**
     * 评论
     */
    @ExcelProperty(value = "评论")
    private String content;


}
