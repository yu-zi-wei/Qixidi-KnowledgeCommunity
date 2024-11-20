package com.aurora.execute;

import com.aurora.business.selector.webSocket.WebSocketInterface;
import com.aurora.business.service.impl.news.NewsUserInfoServiceImpl;
import com.aurora.business.webSocket.WebSocketServer;
import com.aurora.common.enums.WebSocketEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站内聊天
 *
 * @author ziwei
 * @date 2024年01月28日
 */
@Service
public class WebSocketInsideChatExecute implements WebSocketInterface {
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private NewsUserInfoServiceImpl newsUserInfoService;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.INSIDE_CHAT;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {

    }

}
