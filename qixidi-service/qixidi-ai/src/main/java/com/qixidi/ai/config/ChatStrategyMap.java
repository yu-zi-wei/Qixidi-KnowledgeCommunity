package com.qixidi.ai.config;

import com.light.core.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author zi-wei
 * @create 2026/4/27 10:03
 */
@Component
public class ChatStrategyMap {

    @Resource(name = "deepSeekChatClient")
    private ChatClient deepSeekChatClient;

    @Resource(name = "openAiChatClient")
    private ChatClient openAiChatClient;

    private final Map<String, Function<StrategyParams, Flux<String>>> strategyMap;
    private final Map<String, Function<StrategyParams, String>> syncStrategyMap;

    public ChatStrategyMap() {
        this.strategyMap = new HashMap<>();
        this.syncStrategyMap = new HashMap<>();
//        this.strategyMap.put("deepseek", this::executeDeepSeekStrategy);
        this.strategyMap.put("openai", this::executeOpenAiStrategy);
//        this.syncStrategyMap.put("deepseek", this::executeDeepSeekStrategySync);
        this.syncStrategyMap.put("openai", this::executeOpenAiStrategySync);
    }

    public Flux<String> executeStrategy(String modelType, String message,
                                        String conversationId, Boolean openReasoner,
                                        MultipartFile image) {
        Function<StrategyParams, Flux<String>> strategy =
                strategyMap.getOrDefault(modelType.toLowerCase(), this::executeOpenAiStrategy);

        StrategyParams params = new StrategyParams(message, conversationId, openReasoner, image);
        return strategy.apply(params);
    }

    public String executeStrategySync(String modelType, String message,
                                      String conversationId, Boolean openReasoner,
                                      MultipartFile image) {
        Function<StrategyParams, String> strategy =
                syncStrategyMap.getOrDefault(modelType.toLowerCase(), this::executeOpenAiStrategySync);

        StrategyParams params = new StrategyParams(message, conversationId, openReasoner, image);
        return strategy.apply(params);
    }

    /**
     * DeepSeek执行器
     *
     * @param params
     * @return
     */
    private Flux<String> executeDeepSeekStrategy(StrategyParams params) {
        DeepSeekChatOptions chatOptions = DeepSeekChatOptions.builder()
                .model(params.openReasoner ?
                        DeepSeekApi.ChatModel.DEEPSEEK_REASONER.getValue() :
                        DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue())
                .temperature(0.8)
                .build();

        Prompt prompt = new Prompt(params.message, chatOptions);
        AtomicBoolean hasSentSeparator = new AtomicBoolean(false);

        return deepSeekChatClient.prompt(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, params.conversationId))
                .stream()
                .chatResponse()
                .mapNotNull(chatResponse -> {
                    DeepSeekAssistantMessage assistantMessage =
                            (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();

                    String content = getContentFromMessage(assistantMessage);
                    if (StringUtils.isBlank(content)) {
                        return null;
                    }

                    if (assistantMessage.getText() != null && !hasSentSeparator.get()) {
                        hasSentSeparator.set(true);
                        return "--- 思考过程结束 ---" + content;
                    }

                    return content;
                });
    }

    private Flux<String> executeOpenAiStrategy(StrategyParams params) {
        return buildOpenAiRequest(params)
                .stream()
                .content();
    }

    private String executeOpenAiStrategySync(StrategyParams params) {
        String raw = buildOpenAiRequest(params)
                .stream()
                .content()
                .collectList()
                .map(chunks -> String.join("", chunks))
                .block();

        return stripMarkdownCodeBlock(raw);
    }

    private String stripMarkdownCodeBlock(String text) {
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

    private ChatClient.ChatClientRequestSpec buildOpenAiRequest(StrategyParams params) {
        ChatClient.ChatClientRequestSpec request;

        if (params.image != null && !params.image.isEmpty()) {
            request = openAiChatClient.prompt()
                    .user(u -> u.text(params.message)
                            .media(MimeTypeUtils.parseMimeType(params.image.getContentType()),
                                    params.image.getResource()));
        } else {
            request = openAiChatClient.prompt(params.message);
        }

        return request
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, params.conversationId));
    }

    private String getContentFromMessage(DeepSeekAssistantMessage message) {
        if (message.getReasoningContent() != null) {
            return message.getReasoningContent();
        }
        return message.getText();
    }

    private record StrategyParams(String message, String conversationId, Boolean openReasoner,
                                  MultipartFile image) {
    }

}
