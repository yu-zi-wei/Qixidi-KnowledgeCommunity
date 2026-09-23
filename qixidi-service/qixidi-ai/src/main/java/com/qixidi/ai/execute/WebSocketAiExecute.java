package com.qixidi.ai.execute;

import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketInterface;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.ai.config.ChatStrategyMap;
import jakarta.annotation.Resource;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * WebSocket AI 对话执行器（Spring AI 实现）
 * 链路：/websocket/ai/{userId} → WebSocketAiApi → 本类 → ChatStrategyMap 流式策略
 *
 * @author zi-wei
 * @create 2026/9/23
 */
@Slf4j
@Service
public class WebSocketAiExecute implements WebSocketInterface {

    @Resource
    private ChatStrategyMap chatStrategyMap;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.AI;
    }

    @Override
    public void execute(String uuid) {
    }

    @Override
    public void aiExecute(String content, Session session) {
        // 以会话为记忆单元：同一连接内的多轮提问共享上下文
        chatStrategyMap.executeStrategy("openai", content, session.getId(), false, null)
                .doOnNext(chunk -> WebSocketUtils.sendMessage(session, chunk))
                .doOnError(e -> {
                    log.error("WebSocket AI 对话异常：{}", e.getMessage());
                    WebSocketUtils.sendMessage(session, "AI 服务暂时不可用，请稍后重试");
                })
                .doOnComplete(() -> log.info("WebSocket AI 对话完成：sessionId={}", session.getId()))
                .subscribe();
    }
}
