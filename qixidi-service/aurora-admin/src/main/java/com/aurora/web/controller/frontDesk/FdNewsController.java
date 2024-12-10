package com.aurora.web.controller.frontDesk;

import com.aurora.business.domain.bo.news.NewsUserInfoBo;
import com.aurora.business.domain.entity.news.NewsUserRecord;
import com.aurora.business.service.news.INewsUserInfoService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
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
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
