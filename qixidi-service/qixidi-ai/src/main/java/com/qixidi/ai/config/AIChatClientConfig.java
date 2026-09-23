package com.qixidi.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author zi-wei
 * @create 2026/4/27 9:59
 */
@Configuration
public class AIChatClientConfig {
    @Autowired
    private ChatMemoryRepository chatMemoryRepository;

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryRepository(chatMemoryRepository)
                .build();
        return ChatClient.builder(chatModel)
                .defaultSystem("你是栖息地博客的AI助手，请友好、简洁地回答用户的问题")
                .defaultAdvisors(
                        // 日志助手
                        new SimpleLoggerAdvisor(
                                request -> "Custom request: " + request.prompt().getUserMessage(),
                                response -> "Custom response: " + response.getResult(),
                                0),
                        // 记忆助手
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    @Primary
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryRepository(chatMemoryRepository)
                .build();
        return ChatClient.builder(chatModel)
                .defaultSystem("你是DeepSeek助手，请使用贴吧老哥的语气跟我对话")
                .defaultAdvisors(
                        // 日志助手
                        new SimpleLoggerAdvisor(
                                request -> "Custom request: " + request.prompt().getUserMessage(),
                                response -> "Custom response: " + response.getResult(),
                                0),
                        // 记忆助手
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * 业务文本生成专用（文章摘要/总结等）：无记忆、无日志 advisor
     */
    @Bean
    public ChatClient generationChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个文本生成助手，请直接返回纯文本内容，不要使用 Markdown 格式")
                .build();
    }

}
