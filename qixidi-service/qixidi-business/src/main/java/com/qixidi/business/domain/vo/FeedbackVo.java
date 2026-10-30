package com.qixidi.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.Feedback;


/**
 * 用户反馈视图对象 Feedback
 *
 * @author aurora
 * @date 2023-04-17
 */
@Data
@ExcelIgnoreUnannotated
public class FeedbackVo extends Feedback {

    private static final long serialVersionUID = 1L;

    /**
     * 更新人名称
     */
    private String updateName;

}

