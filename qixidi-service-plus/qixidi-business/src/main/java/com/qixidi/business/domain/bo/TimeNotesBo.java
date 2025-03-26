package com.qixidi.business.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author zi-wei
 * @create 2025/3/26 10:12
 */
@Data
public class TimeNotesBo {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * uid
     */
    private String uid;

    /**
     * 记录时间
     */
    @NotNull(message = "记录时间不能为空")
    private LocalDate recordTime;
}
