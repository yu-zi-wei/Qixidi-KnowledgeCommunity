package com.aurora.execute;

import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.constant.WebSocketConstant;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.business.selector.webSocket.WebSocketInterface;
import com.aurora.business.service.privateUser.IPrivateUserService;
import com.aurora.business.webSocket.WebSocketServer;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.WebSocketEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zi-wei
 * @create 2024/12/11 15:00
 */
@Service
public class WebsocketPersonalRedDotExecute implements WebSocketInterface {
    @Autowired
    private IPrivateUserService iPrivateUserService;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.PERSONAL_RED_DOT;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {
        PrivateUserBo bo = new PrivateUserBo();
        bo.setUid(uuid.replace(WebSocketConstant.PERSONAL_RED_DOT, ""));
        TableDataInfo<PrivateUserVo> list = iPrivateUserService.queryPageList(bo, new PageQuery());
        webSocketServer.sendMessageToUser(uuid, list);
    }
}
