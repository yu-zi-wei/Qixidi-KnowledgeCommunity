package com.aurora.web.controller.frontDesk.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.business.service.privateUser.IPrivateUserService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Validated
@Api(value = "私信记录控制器", tags = {"私信记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/private/user")
public class FdPrivateUserController extends BaseController {

    private final IPrivateUserService iPrivateUserService;

    /**
     * 查询私信用户记录表
     */
    @ApiOperation("查询私信用户记录表")
    @GetMapping("/list")
    public TableDataInfo<PrivateUserVo> list(@Validated(QueryGroup.class) PrivateUserBo bo, PageQuery pageQuery) {
        return iPrivateUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增私信用户记录表
     */
    @ApiOperation("新增私信用户记录表")
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @GetMapping("add/{targetUid}")
    public R<Void> add(@NotEmpty(message = "目标id不能为空") @PathVariable("targetUid") String targetUid) {
        return toAjax(iPrivateUserService.addPrivateUser(targetUid) ? 1 : 0);
    }

    /**
     * 删除私信用户
     */
    @ApiOperation("删除私信记录")
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable String id) {
        return toAjax(iPrivateUserService.deleteById(id) ? 1 : 0);
    }

    /**
     * 删除所有私信用户
     */
    @ApiOperation("删除私信记录")
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/all")
    public R<Void> remove() {
        return toAjax(iPrivateUserService.deleteAll() ? 1 : 0);
    }

}
