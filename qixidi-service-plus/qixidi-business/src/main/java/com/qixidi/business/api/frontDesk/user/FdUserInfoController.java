package com.qixidi.business.api.frontDesk.user;

import com.light.core.core.domain.R;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.user.CreatorApplicationBo;
import com.qixidi.business.domain.bo.user.UserBindBo;
import com.qixidi.business.domain.bo.user.UserInfoBo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.service.ITripartiteUserService;
import com.qixidi.system.domain.entity.SysOss;
import com.qixidi.system.service.ISysOssService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 【前台】用户个人信息管理
 *
 * @author zi-wei
 * @create 2025/11/14 18:02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/front-desk/user")
public class FdUserInfoController extends BaseController {

    private final ITripartiteUserService iTripartiteUserService;
    private final ISysOssService iSysOssService;

    /**
     * 获取用户详细信息
     *
     * @return
     */
    @GetMapping("/info")
    public R<TripartiteUserVo> websiteInfo() {
        return R.ok(iTripartiteUserService.websiteInfo());
    }

    /**
     * 获取用户基础信息
     *
     * @return
     */
    @GetMapping("/basics")
    public R<TripartiteUserVo> getBasicsUser() {
        return R.ok(iTripartiteUserService.BasicsUser());
    }


    /**
     * 前台用户头像上传
     *
     * @param multipartHttpServletRequest
     * @param req
     * @return
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/update/avatar")
    public R<Map<String, Object>> avatar(MultipartHttpServletRequest multipartHttpServletRequest, HttpServletRequest req) {
        Map<String, Object> ajax = new HashMap<>();
        Map<String, MultipartFile> files = multipartHttpServletRequest.getFileMap();
        //得到头像文件
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return R.fail("头像修改失败，可到反馈专区留言！");
        MultipartFile file = files.get("img");
        if (!file.isEmpty()) {
            SysOss oss = iSysOssService.upload(file);
            String avatar = oss.getUrl();
            if (iTripartiteUserService.updateUserAvatar(uuid, avatar)) {
                ajax.put("imgUrl", avatar);
                return R.ok(ajax);
            }
        }
        return R.fail("头像修改失败，可到反馈专区留言！");
    }

    /**
     * 修改用户信息
     */
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/info")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserInfoBo bo) {
        return toAjax(iTripartiteUserService.updateUserInfo(bo) ? 1 : 0);
    }

    /**
     * 修改邮箱手机号
     *
     * @param bo
     * @return
     */
    @Log(title = "修改邮箱手机号", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/email")
    public R<Void> bindEmail(@Validated(EditGroup.class) @RequestBody UserBindBo bo) {
        bo.setUuid(LoginHelper.getTripartiteUuid());
        return toAjax(iTripartiteUserService.bindEmail(bo) ? 1 : 0);
    }

    /**
     * 创作者申请
     *
     * @param bo
     * @return
     */
    @Log(title = "创作者申请", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/creator/application")
    public R<Void> creatorApplication(@Validated @RequestBody CreatorApplicationBo bo) {
        iTripartiteUserService.creatorApplication(bo);
        return R.ok();
    }

}
