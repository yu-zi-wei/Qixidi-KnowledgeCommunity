package com.qixidi.system.api.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.light.core.core.domain.R;
import com.light.core.core.domain.dto.UserOnlineDTO;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.system.domain.entity.SysUserOnline;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线用户监控管理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    /**
     * 在线用户列表
     *
     * @param ipaddr
     * @param userName
     * @return
     */
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public TableDataInfo<SysUserOnline> list(String ipaddr, String userName) {
//        // 获取所有未过期的 token
//        List<String> keys = StpUtil.searchTokenValue( );
        List<UserOnlineDTO> userOnlineDTOList = new ArrayList<>();
//        for (String key : keys) {
//            String token = key.replace(Constants.LOGIN_TOKEN_KEY, "");
//            // 如果已经过期则踢下线
//            if (StpUtil.stpLogic.getTokenActivityTimeoutByToken(token) < 0) {
//                continue;
//            }
//            userOnlineDTOList.add(RedisUtils.getCacheObject(Constants.ONLINE_TOKEN_KEY + token));
//        }
//        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
//            userOnlineDTOList = userOnlineDTOList.stream().filter(userOnline ->
//                StringUtils.equals(ipaddr, userOnline.getIpaddr()) &&
//                    StringUtils.equals(userName, userOnline.getUserName())
//            ).collect(Collectors.toList());
//        } else if (StringUtils.isNotEmpty(ipaddr)) {
//            userOnlineDTOList = userOnlineDTOList.stream().filter(userOnline ->
//                    StringUtils.equals(ipaddr, userOnline.getIpaddr()))
//                .collect(Collectors.toList());
//        } else if (StringUtils.isNotEmpty(userName)) {
//            userOnlineDTOList = userOnlineDTOList.stream().filter(userOnline ->
//                StringUtils.equals(userName, userOnline.getUserName())
//            ).collect(Collectors.toList());
//        }
//        Collections.reverse(userOnlineDTOList);
//        userOnlineDTOList.removeAll(Collections.singleton(null));
        List<SysUserOnline> userOnlineList = BeanUtil.copyToList(userOnlineDTOList, SysUserOnline.class);
        return TableDataInfo.build(userOnlineList);
    }

    /**
     * 强退用户
     */
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public R<Void> forceLogout(@PathVariable String tokenId) {
        try {
            StpUtil.kickoutByTokenValue(tokenId);
        } catch (NotLoginException e) {
        }
        return R.ok();
    }
}
