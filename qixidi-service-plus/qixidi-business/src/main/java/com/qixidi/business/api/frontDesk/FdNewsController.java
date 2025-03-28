package com.qixidi.business.api.frontDesk;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.service.news.INewsUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 【前台】用户消息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/news")
public class FdNewsController extends BaseController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息汇总
     */
    @GetMapping("/list/sum")
    public R listSum() {
        return R.ok(iNewsUserInfoService.listSum());
    }

    @GetMapping("/list/info")
    public R listInfo() {
        return R.ok(iNewsUserInfoService.listInfo());
    }

    /**
     * 查询用户消息列表
     */
    @GetMapping("/list")
    public R userList(NewsUserInfoBo bo, PageQuery pageQuery) {
        return R.ok(iNewsUserInfoService.userList(bo, pageQuery));
    }

    /**
     * 新增消息
     */
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NewsUserInfoBo bo) {
        return toAjax(iNewsUserInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 消息已读
     */
    @Log(title = "用户消息", businessType = BusinessType.UPDATE)
    @GetMapping("/news-read")
    public R<Void> newsRead(NewsUserRecord bo) {
        return toAjax(iNewsUserInfoService.newsRead(bo) ? 1 : 0);
    }

    /**
     * 删除消息
     */
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
