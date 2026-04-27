package com.qixidi.ai.controller;

import com.qixidi.ai.config.ChatStrategyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author zi-wei
 * @create 2026/4/27 10:13
 */
@RestController
@RequestMapping("/ai")
public class AIChatController {

    @Autowired
    private ChatStrategyMap executeStrategy;

    /**
     * 流式对话接口
     *
     * @param message        消息内容
     * @param conversationId 会话ID
     * @param modelType      模型类型：bailian（百链），deepSeek
     * @param openReasoner   是否开启推理模型（仅对deepSeek有效）
     * @return 流式响应
     */
    @GetMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                                       @RequestParam(value = "conversationId", defaultValue = "1") String conversationId,
                                       @RequestParam(value = "modelType", defaultValue = "bailian") String modelType,
                                       @RequestParam(value = "openReasoner", defaultValue = "false") Boolean openReasoner) {
        return executeStrategy.executeStrategy(modelType, message, conversationId, openReasoner);
    }

}
