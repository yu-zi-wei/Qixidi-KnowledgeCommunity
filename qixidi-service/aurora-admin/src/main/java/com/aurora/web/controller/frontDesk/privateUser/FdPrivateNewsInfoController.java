package com.aurora.web.controller.frontDesk.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.aurora.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.aurora.business.service.privateUser.IPrivateNewsInfoService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

@Validated
@Api(value = "私信记录控制器", tags = {"私信记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/private/newsInfo")
public class FdPrivateNewsInfoController extends BaseController {


    private final IPrivateNewsInfoService iPrivateNewsInfoService;

    /**
     * 查询私信记录列表
     */
    @ApiOperation("查询私信记录列表")
    @GetMapping("/list")
    public TableDataInfo<PrivateNewsInfoVo> list(@Validated(QueryGroup.class) PrivateNewsInfoBo bo, PageQuery pageQuery) {
        return iPrivateNewsInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增私信记录
     */
    @ApiOperation("新增私信记录")
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrivateNewsInfoBo bo) {
        return toAjax(iPrivateNewsInfoService.insertByBo(bo) ? 1 : 0);
    }


    /**
     * 私信已读
     */
    @ApiOperation("私信已读")
    @GetMapping("/been/read/{targetUid}")
    public void beenRead(@NotEmpty(message = "目标id不能为空") @PathVariable("targetUid") String targetUid) {
        iPrivateNewsInfoService.beenRead(targetUid);
    }

    /**
     * 删除私信记录
     */
    @ApiOperation("删除私信记录")
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iPrivateNewsInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
