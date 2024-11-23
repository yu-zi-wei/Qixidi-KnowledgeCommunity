package com.aurora.web.controller.frontDesk;

import com.aurora.business.domain.vo.news.NewsUserSumVo;
import com.aurora.business.domain.vo.user.UserSimpleInfoVo;
import com.aurora.business.service.news.INewsUserInfoService;
import com.aurora.common.core.domain.R;
import com.aurora.common.utils.SseEmitterUtil;
import com.aurora.business.service.ITripartiteUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Validated
@Api(value = "SSE消息推送控制层", tags = {"SSE消息推送"})
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/frontDesk/sse")
public class SseEmitterController {

    private final INewsUserInfoService iNewsUserInfoService;
    private final ITripartiteUserService iTripartiteUserService;

    /**
     * 用于创建连接
     */
    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {
//        Boolean aBoolean = SseEmitterUtil.IsOnLine(userId);
//        if (aBoolean) return null;
        SseEmitter connect = SseEmitterUtil.connect(userId);
        return connect;
    }

    /**
     * 推送给所有人
     *s
     * @param message
     * @return
     */
    @GetMapping("/push/{message}")
    public R<String> push(@PathVariable(name = "message") String message) {
        //获取连接人数
        int userCount = SseEmitterUtil.getUserCount();
        //如果无在线人数，返回
        if (userCount < 1) {
            return R.fail("无人在线！");
        }
        SseEmitterUtil.batchSendMessage(message);
        return R.ok("发送成功！");
    }

    /**
     * 发送给单个人
     *
     * @param userid
     * @return
     */
    @GetMapping("/push_one/{userid}")
    public R<String> pushOne(@PathVariable(name = "userid") String userid) {
        List<NewsUserSumVo> list = iNewsUserInfoService.pushOne(userid);
        return R.ok("推送消息给" + userid);
    }

    /**
     * 当前私信用户是否在线
     *
     * @param userid
     * @return
     */
    @GetMapping("/is/online/{userid}")
    public R<UserSimpleInfoVo> isOnline(@PathVariable(name = "userid") String userid) {
//        获取真正的uid
        String uid = userid.substring(0, userid.lastIndexOf(":sx"));
        UserSimpleInfoVo userInformation = iTripartiteUserService.isOnline(uid);
        userInformation.setIsOnline(SseEmitterUtil.IsOnLine(userid));
        return R.ok(userInformation);
    }

    /**
     * 发送给单个人(测试)
     *
     * @param userid
     * @return
     */
    @GetMapping("/push_one/{mag}/{userid}")
    public R<String> pushOne(@PathVariable(name = "userid") String userid, @PathVariable(name = "mag") String mag) {
        SseEmitterUtil.sendMessage(userid, mag);
        return R.ok("推送消息给" + userid);
    }

    /**
     * 关闭连接
     */
    @GetMapping("/close/{userid}")
    public R<String> close(@PathVariable("userid") String userid) {
        SseEmitterUtil.removeUser(userid);
        return R.ok("连接关闭");
    }

}
