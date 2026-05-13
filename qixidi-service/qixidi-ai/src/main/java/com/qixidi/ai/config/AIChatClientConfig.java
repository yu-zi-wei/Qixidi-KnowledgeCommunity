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
                .defaultSystem("你是一个图片解析助手，请返回json格式的解析数据")
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

}
