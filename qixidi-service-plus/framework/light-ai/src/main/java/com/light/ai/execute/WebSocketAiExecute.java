package com.light.ai.execute;

import com.light.ai.service.DeepSeekService;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketInterface;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zi-wei
 * @create 2025/2/27 17:21
 */
@Service
public class WebSocketAiExecute implements WebSocketInterface {
    @Autowired
    private DeepSeekService deepSeekService;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.AI;
    }

    @Override
    public void aiExecute(String content, Session session) {
        deepSeekService.generationContent(content, session);
    }

    @Override
    public void execute(String uuid) {
    }
}
