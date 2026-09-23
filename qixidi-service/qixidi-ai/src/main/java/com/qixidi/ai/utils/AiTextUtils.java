package com.qixidi.ai.utils;

import com.light.core.utils.StringUtils;

/**
 * AI 返回内容清洗工具
 *
 * @author zi-wei
 * @create 2026/9/23
 */
public class AiTextUtils {

    private AiTextUtils() {
    }

    /**
     * 剥离模型返回内容外层的 Markdown 代码块包裹（```...```）
     *
     * @param text 模型返回的原始文本
     * @return 剥离后的纯文本
     */
    public static String stripMarkdownCodeBlock(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0) {
                trimmed = trimmed.substring(firstNewline + 1);
            }
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3);
            }
        }
        return trimmed.trim();
    }
}
