package com.qixidi.ai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qixidi.ai.config.ChatStrategyMap;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

/**
 * @author zi-wei
 * @create 2026/4/27 10:13
 */
@RestController
@RequestMapping("/ai")
public class AIChatController {

    @Resource
    private ChatStrategyMap executeStrategy;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 流式对话接口（支持图片）
     *
     * @param message        消息内容
     * @param conversationId 会话ID
     * @param modelType      模型类型：bailian（百链），deepSeek
     * @param openReasoner   是否开启推理模型（仅对deepSeek有效）
     * @param image          图片文件（可选，支持 jpg/png/gif）
     * @return 流式响应
     */
    @PostMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                                       @RequestParam(value = "conversationId", defaultValue = "1") String conversationId,
                                       @RequestParam(value = "modelType", defaultValue = "openai") String modelType,
                                       @RequestParam(value = "openReasoner", defaultValue = "false") Boolean openReasoner,
                                       @RequestPart(value = "image", required = false) MultipartFile image) {
        return executeStrategy.executeStrategy(modelType, message, conversationId, openReasoner, image);
    }

    /**
     * 文本返回（支持图片）
     *
     * @param message        消息内容
     * @param conversationId 会话ID
     * @param modelType      模型类型：bailian（百链），deepSeek
     * @param openReasoner   是否开启推理模型（仅对deepSeek有效）
     * @param image          图片文件（可选，支持 jpg/png/gif）
     * @return 流式响应
     */
    @PostMapping(value = "/generate")
    public Object generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                           @RequestParam(value = "conversationId", defaultValue = "1") String conversationId,
                           @RequestParam(value = "modelType", defaultValue = "openai") String modelType,
                           @RequestParam(value = "openReasoner", defaultValue = "false") Boolean openReasoner,
                           @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {
        String result = executeStrategy.executeStrategySync(modelType, message, conversationId, openReasoner, image);
        return objectMapper.readValue(result, Object.class);
    }
}
