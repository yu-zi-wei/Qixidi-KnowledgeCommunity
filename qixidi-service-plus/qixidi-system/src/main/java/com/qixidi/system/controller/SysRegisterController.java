package com.qixidi.system.controller;

import com.light.core.core.domain.R;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.auth.domain.model.RegisterBody;
import com.qixidi.system.service.ISysConfigService;
import com.qixidi.system.service.SysRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysRegisterController extends BaseController {

    private final SysRegisterService registerService;
    private final ISysConfigService configService;

    /**
     * 后台用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/admin/register")
    public R<Void> register(@Validated @RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        registerService.register(user);
        return R.ok();
    }
}
