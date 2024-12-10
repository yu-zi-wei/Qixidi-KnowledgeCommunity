package com.aurora.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 用户反馈视图对象 Feedback
 *
 * @author aurora
 * @date 2023-04-17
 */
@Data
@ExcelIgnoreUnannotated
public class FeedbackVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "")
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
     * 反馈内容
     */
    @ExcelProperty(value = "反馈内容")
    private String FeedbackContent;

    /**
     * 反馈标题
     */
    private String FeedbackTitle;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新人名称
     */
    private String updateName;

}

