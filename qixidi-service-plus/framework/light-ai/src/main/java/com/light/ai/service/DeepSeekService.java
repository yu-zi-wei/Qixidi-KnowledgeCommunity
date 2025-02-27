package com.light.ai.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.light.ai.config.DeepSeekConfig;
import com.light.webSocket.utils.WebSocketUtils;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zi-wei
 * @create 2025/2/27 15:38
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DeepSeekService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DeepSeekConfig deepSeekConfig;

    public void generationContent(String questions, Session session) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> message = new HashMap<>();

        String url = deepSeekConfig.getUrl();
        requestBody.put("model", deepSeekConfig.getModel());
        message.put("role", deepSeekConfig.getRole());
        message.put("content", questions);
        requestBody.put("messages", new Object[]{message});
        requestBody.put("stream", true);

        WebClient webClient = WebClient.create();
        Flux<String> authorization = webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + deepSeekConfig.getAuthorizationKey())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class);// 使用 bodyToFlux 方法来处理流式响应

        authorization.subscribe(
                chunk -> {
                    try {
                        if (!chunk.equals("[DONE]")) {
                            //数据清洗
                            Map<String, Object> jsonObj = objectMapper.readValue(chunk, Map.class);
                            List<Object> choicesList = (List<Object>) jsonObj.get("choices");
                            Object choicesObj = choicesList.get(0);
                            JSONObject entries = JSONUtil.parseObj(choicesObj);
                            Object data = entries.get("delta");
                            JSONObject dataJson = JSONUtil.parseObj(data);

                            //webSocket消息推送
                            WebSocketUtils.sendMessage(session, dataJson.get("reasoning_content"));
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    log.error("响应异常：{} ", error);
                    throw new RuntimeException("响应异常：{}", error);
                },
                () -> {
                    log.info("相应结束");
                }
        );
    }
}
