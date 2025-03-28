package com.qixidi.business.api.frontDesk.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.service.privateUser.IPrivateUserService;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotEmpty;

/**
 * 【前台】私信用户管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/private/user")
public class FdPrivateUserController extends BaseController {

    private final IPrivateUserService iPrivateUserService;

    /**
     * 查询私信用户记录表
     */
    @GetMapping("/list")
    public TableDataInfo<PrivateUserVo> list(@Validated(QueryGroup.class) PrivateUserBo bo, PageQuery pageQuery) {
        return iPrivateUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增私信用户记录表
     */
    @RepeatSubmit()
    @GetMapping("add/{targetUid}")
    public R<Void> add(@NotEmpty(message = "目标id不能为空") @PathVariable("targetUid") String targetUid) {
        return toAjax(iPrivateUserService.addPrivateUser(targetUid) ? 1 : 0);
    }

    /**
     * 删除私信用户
     */
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable String id) {
        return toAjax(iPrivateUserService.deleteById(id) ? 1 : 0);
    }

    /**
     * 删除所有私信用户
     */
    @DeleteMapping("/all")
    public R<Void> remove() {
        return toAjax(iPrivateUserService.deleteAll() ? 1 : 0);
    }

}
