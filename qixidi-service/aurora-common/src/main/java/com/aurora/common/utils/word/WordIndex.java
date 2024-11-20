package com.aurora.common.utils.word;

import lombok.Data;

import java.util.List;

/**
 * 敏感词标记类
 */
@Data
public class WordIndex {

    /**
     * 标记结果
     */
    private boolean isBlackWord;
    /**
     * 标记索引
     */
    private List<Integer> index;

    /**
     * 敏感词
     */
    private String word;
}
