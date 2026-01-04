package com.qixidi.system.api;

import com.light.exception.ServiceException;

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
public class SysRegisterController {

    private final SysRegisterService registerService;
    private final ISysConfigService configService;

    /**
     * 后台用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("/admin/register")
    public void register(@Validated @RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            throw new ServiceException("当前系统没有开启注册功能");
        }
        registerService.register(user);
    }
}
