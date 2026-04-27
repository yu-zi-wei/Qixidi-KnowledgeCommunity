package com.qixidi.ai.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author zi-wei
 * @create 2026/4/27 10:03
 */
@Component
public class ChatStrategyMap {

//    @Resource(name = "deepSeekChatClient")
//    private ChatClient deepSeekChatClient;

    @Resource(name = "openAiChatClient")
    private ChatClient openAiChatClient;

    private final Map<String, Function<StrategyParams, Flux<String>>> strategyMap;

    public ChatStrategyMap() {
        this.strategyMap = new HashMap<>();
//        this.strategyMap.put("deepseek", this::executeDeepSeekStrategy);
        this.strategyMap.put("bailian", this::executeBaiLianStrategy);
    }

    public Flux<String> executeStrategy(String modelType, String message,
                                        String conversationId, Boolean openReasoner) {
        Function<StrategyParams, Flux<String>> strategy =
                strategyMap.getOrDefault(modelType.toLowerCase(), this::executeBaiLianStrategy);

        StrategyParams params = new StrategyParams(message, conversationId, openReasoner);
        return strategy.apply(params);
    }

    /**
     * DeepSeek执行器
     *
     * @param params
     * @return
     */
//    private Flux<String> executeDeepSeekStrategy(StrategyParams params) {
//        DeepSeekChatOptions chatOptions = DeepSeekChatOptions.builder()
//                .model(params.openReasoner ?
//                        DeepSeekApi.ChatModel.DEEPSEEK_REASONER.getValue() :
//                        DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue())
//                .temperature(0.8)
//                .build();
//
//        Prompt prompt = new Prompt(params.message, chatOptions);
//        AtomicBoolean hasSentSeparator = new AtomicBoolean(false);
//
//        return deepSeekChatClient.prompt(prompt)
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, params.conversationId))
//                .stream()
//                .chatResponse()
//                .mapNotNull(chatResponse -> {
//                    DeepSeekAssistantMessage assistantMessage =
//                            (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();
//
//                    String content = getContentFromMessage(assistantMessage);
//                    if (StringUtils.isBlank(content)) {
//                        return null;
//                    }
//
//                    if (assistantMessage.getText() != null && !hasSentSeparator.get()) {
//                        hasSentSeparator.set(true);
//                        return "--- 思考过程结束 ---" + content;
//                    }
//
//                    return content;
//                });
//    }

    /**
     * openAi 执行器
     *
     * @param params
     * @return
     */
    private Flux<String> executeBaiLianStrategy(StrategyParams params) {
        return openAiChatClient.prompt(params.message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, params.conversationId))
                .stream()
                .content();
    }

//    private String getContentFromMessage(DeepSeekAssistantMessage message) {
//        if (message.getReasoningContent() != null) {
//            return message.getReasoningContent();
//        }
//        return message.getText();
//    }

    private record StrategyParams(String message, String conversationId, Boolean openReasoner) {
    }

}
