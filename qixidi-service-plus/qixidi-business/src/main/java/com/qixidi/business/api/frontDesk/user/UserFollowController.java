package com.qixidi.business.api.frontDesk.user;

import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.business.domain.bo.user.UserFollowBo;
import com.qixidi.business.service.user.IUserFollowService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】用户关注管理
 *
 * @author aurora
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/follow")
public class UserFollowController extends BaseController {

    private final IUserFollowService iUserFollowService;

    /**
     * 新增关注
     */
    @Log(title = "新增关注", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserFollowBo bo) {
        return toAjax(iUserFollowService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 取消关注
     *
     * @param bo
     * @return
     */
    @Log(title = "取消关注", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/cancel")
    public R<Void> cancelFollow(@Validated(AddGroup.class) @RequestBody UserFollowBo bo) {
        return toAjax(iUserFollowService.cancelFollow(bo) ? 1 : 0);
    }

    /**
     * 用户关注列表
     *
     * @param type
     * @return
     */
    @GetMapping("/list/{type}")
    public R followRoleList(@NotNull(message = "类型不能为空") @PathVariable("type") Integer type) {
        return R.ok(iUserFollowService.followRoleList(type));
    }
}
