package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.domain.bo.user.UserBindBo;
import com.aurora.business.domain.bo.user.UserInfoBo;
import com.aurora.business.domain.entity.count.CountUserWebsiteEntity;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.business.service.user.IUserFollowService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.helper.LoginHelper;
import com.aurora.system.domain.SysOss;
import com.aurora.system.service.ISysOssService;
import com.aurora.system.service.ITripartiteUserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Validated
@Api(value = "前端用户信息控制器", tags = {"用户信息接口"})
@RequiredArgsConstructor
@RestController
public class UserWebInfoController extends BaseController {

    private final ITripartiteUserService iTripartiteUserService;
    private final ISysOssService iSysOssService;
    private final IUserFollowService iUserFollowService;


    @ApiOperation("用户关注列表")
    @GetMapping("/white/user/follow/list/{uid}/{type}")
    public R followList(@NotBlank(message = "uid不能为空") @PathVariable("uid") String uid,
                        @NotNull(message = "类型不能为空") @PathVariable("type") Integer type) {
        return R.ok(iUserFollowService.followList(uid, type));
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/white/user/list")
    public List<TripartiteUserVo> fdUserList(UserInfoBo bo) {
        return iTripartiteUserService.fdUserList(bo);
    }

    @ApiOperation("获取用户个人数据")
    @GetMapping("/white/user/data/{uuid}")
    public R<CountUserWebsiteEntity> fdUserData(@NotNull(message = "主键不能为空")
                                                @PathVariable("uuid") String uuid) {
        return R.ok(iTripartiteUserService.fdUserData(uuid));
    }

    @ApiOperation("获取用户详细信息")
    @GetMapping("/front-desk/user/info")
    public R<TripartiteUserVo> websiteInfo() {
        return R.ok(iTripartiteUserService.websiteInfo());
    }

    @ApiOperation("获取用户基础信息")
    @GetMapping("/front-desk/user/basics")
    public R<TripartiteUserVo> getBasicsUser() {
        return R.ok(iTripartiteUserService.BasicsUser());
    }

    @ApiOperation("通过用户id获取用户详细信息")
    @GetMapping("/white/user/info/{uuid}")
    public R<TripartiteUserVo> getWebsiteInfo(@ApiParam("主键")
                                              @NotNull(message = "主键不能为空")
                                              @PathVariable("uuid") String uuid) {
        return R.ok(iTripartiteUserService.getWebsiteInfo(uuid));
    }


    /**
     * 头像上传
     */
//    @ApiOperation("前台用户头像上传")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "avatarfile", value = "用户头像", paramType = "query", dataTypeClass = File.class, required = true)
//    })
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping("/update/user/avatar")
//    public R<Map<String, Object>> avatar(@RequestPart("avatarfile") MultipartFile file) {
//        Map<String, Object> ajax = new HashMap<>();
//        if (!file.isEmpty()) {
//            SysOss oss = iSysOssService.upload(file);
//            String avatar = oss.getUrl();
//            if (iTripartiteUserService.updateUserAvatar(LoginHelper.getTripartiteUuid(), avatar)) {
//                ajax.put("imgUrl", avatar);
//                return R.ok(ajax);
//            }
//        }
//        return R.fail("头像修改失败，可到反馈专区留言！");
//    }
    @ApiOperation("前台用户头像上传")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "avatarfile", value = "用户头像", paramType = "query", dataTypeClass = File.class, required = true)
    })
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/update/user/avatar")
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
    @ApiOperation("修改用户信息")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/user/info")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserInfoBo bo) {
        return toAjax(iTripartiteUserService.updateUserInfo(bo) ? 1 : 0);
    }

    @ApiOperation("修改邮箱手机号")
    @Log(title = "修改邮箱手机号", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/update/user/email")
    public R<Void> bindEmail(@Validated(EditGroup.class) @RequestBody UserBindBo bo) {
        bo.setUuid(LoginHelper.getTripartiteUuid());
        return toAjax(iTripartiteUserService.bindEmail(bo) ? 1 : 0);
    }
}
