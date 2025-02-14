package com.qixidi.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysBlackListDto {
    /**
     * 记录时间戳
     */
    private Long timestamp;
    /**
     * 时间间隔（秒）
     */
    private Long timeInterval = 0L;
    /**
     * 请求url列表
     */
    private List<String> urlList = new ArrayList<>();
    /**
     * 列表长度
     */
    private Integer ipLisSize = 0;

    public SysBlackListDto(Long timestamp) {
        this.timestamp = timestamp;
    }
}
