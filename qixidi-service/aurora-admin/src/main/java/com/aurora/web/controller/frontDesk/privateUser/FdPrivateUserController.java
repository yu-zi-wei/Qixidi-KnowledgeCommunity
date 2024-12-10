package com.aurora.web.controller.frontDesk.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.business.service.privateUser.IPrivateUserService;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

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
