package com.qixidi.system.api;

import cn.dev33.satoken.secure.BCrypt;
import com.light.core.constant.UserConstants;
import com.light.core.enums.BusinessType;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.domain.entity.SysUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.system.domain.entity.SysOss;
import com.qixidi.system.service.ISysOssService;
import com.qixidi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController {

    private final ISysUserService userService;
    private final ISysOssService iSysOssService;

    /**
     * 个人信息
     */
    @GetMapping
    public Map<String, Object> profile() {
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
        return ajax;
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public void updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            throw new ServiceException("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(LoginHelper.getUserId());
        user.setUserName(null);
        user.setPassword(null);
        if (userService.updateUserProfile(user) <= 0) {
            throw new ServiceException("修改个人信息异常，请联系管理员");
        }
    }

    /**
     * 重置密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public void updatePwd(String oldPassword, String newPassword) {
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        String userName = user.getUserName();
        String password = user.getPassword();
        if (!BCrypt.checkpw(oldPassword, password)) {
            throw new ServiceException("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(newPassword, password)) {
            throw new ServiceException("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, BCrypt.hashpw(newPassword)) <= 0) {
            throw new ServiceException("修改密码异常，请联系管理员");
        }
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public Map<String, Object> avatar(@RequestPart("avatarfile") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException("上传图片异常，请联系管理员");
        }
        Map<String, Object> ajax = new HashMap<>();
        SysOss oss = iSysOssService.upload(file);
        String avatar = oss.getUrl();
        if (userService.updateUserAvatar(LoginHelper.getUsername(), avatar)) {
            ajax.put("imgUrl", avatar);
        }
        return ajax;
    }
}
