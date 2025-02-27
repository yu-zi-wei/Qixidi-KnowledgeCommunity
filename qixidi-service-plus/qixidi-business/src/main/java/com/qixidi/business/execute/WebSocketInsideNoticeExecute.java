package com.qixidi.business.execute;


import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.light.webSocket.selector.WebSocketInterface;
import com.qixidi.business.service.impl.news.NewsUserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
@Service
public class WebSocketInsideNoticeExecute implements WebSocketInterface {
    @Autowired
    private NewsUserInfoServiceImpl newsUserInfoService;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.INSIDE_NOTICE;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {
        List<NewsUserSumVo> list = newsUserInfoService.pushOne(uuid);
        WebSocketUtils.sendMessage(uuid, list);
    }
}
