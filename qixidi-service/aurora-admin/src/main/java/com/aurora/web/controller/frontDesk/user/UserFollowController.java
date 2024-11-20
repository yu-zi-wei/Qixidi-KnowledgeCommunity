package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.domain.bo.user.UserFollowBo;
import com.aurora.business.service.user.IUserFollowService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 用户关注Controller
 *
 * @author aurora
 * @date 2023-02-13
 */
@Validated
@Api(value = "用户关注控制器", tags = {"用户关注管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/follow")
public class UserFollowController extends BaseController {

    private final IUserFollowService iUserFollowService;

    /**
     * 新增关注
     */
    @ApiOperation("新增关注")
    @Log(title = "新增关注", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserFollowBo bo) {
        return toAjax(iUserFollowService.insertByBo(bo) ? 1 : 0);
    }

    @ApiOperation("取消关注")
    @Log(title = "取消关注", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/cancel")
    public R<Void> cancelFollow(@Validated(AddGroup.class) @RequestBody UserFollowBo bo) {
        return toAjax(iUserFollowService.cancelFollow(bo) ? 1 : 0);
    }

    @ApiOperation("用户关注列表")
    @GetMapping("/list/{type}")
    public R followRoleList(@NotNull(message = "类型不能为空") @PathVariable("type") Integer type) {
        return R.ok(iUserFollowService.followRoleList(type));
    }
}
