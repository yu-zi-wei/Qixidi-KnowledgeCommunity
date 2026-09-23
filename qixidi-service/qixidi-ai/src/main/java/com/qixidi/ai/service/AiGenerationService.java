package com.qixidi.ai.service;

import com.qixidi.ai.utils.AiTextUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * AI 内容生成服务（无会话记忆）
 * 用于文章摘要/总结等业务生成场景，区别于 ChatStrategyMap 的多轮对话
 *
 * @author zi-wei
 * @create 2026/9/23
 */
@Slf4j
@Service
public class AiGenerationService {

    /**
     * 按名注入：绕开 @Primary 的 deepSeekChatClient
     */
    @Resource(name = "generationChatClient")
    private ChatClient generationChatClient;

    /**
     * 同步生成文本内容
     *
     * @param prompt 生成提示词
     * @return 生成的内容，失败返回 null（调用方跳过后续更新，不影响主流程）
     */
    public String generateContent(String prompt) {
        try {
            return AiTextUtils.stripMarkdownCodeBlock(generationChatClient.prompt(prompt).call().content());
        } catch (Exception e) {
            log.warn("AI 内容生成失败：{}", e.getMessage());
            return null;
        }
    }
}
