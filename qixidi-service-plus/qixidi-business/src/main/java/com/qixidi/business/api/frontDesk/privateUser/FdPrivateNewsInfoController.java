package com.qixidi.business.api.frontDesk.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.qixidi.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.qixidi.business.service.privateUser.IPrivateNewsInfoService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * 【前台】私信记录管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/private/newsInfo")
public class FdPrivateNewsInfoController extends BaseController {


    private final IPrivateNewsInfoService iPrivateNewsInfoService;

    /**
     * 查询私信记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<PrivateNewsInfoVo> list(@Validated(QueryGroup.class) PrivateNewsInfoBo bo, PageQuery pageQuery) {
        return iPrivateNewsInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PrivateNewsInfoBo bo) {
        return toAjax(iPrivateNewsInfoService.insertByBo(bo) ? 1 : 0);
    }


    /**
     * 私信已读
     */
    @GetMapping("/been/read/{targetUid}")
    public void beenRead(@NotEmpty(message = "目标id不能为空") @PathVariable("targetUid") String targetUid) {
        iPrivateNewsInfoService.beenRead(targetUid);
    }

    /**
     * 删除私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iPrivateNewsInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
