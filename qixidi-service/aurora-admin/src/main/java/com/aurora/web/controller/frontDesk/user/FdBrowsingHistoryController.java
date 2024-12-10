package com.aurora.web.controller.frontDesk.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.user.BrowsingHistoryBo;
import com.aurora.business.domain.vo.user.BrowsingHistoryVo;
import com.aurora.business.service.IBrowsingHistoryService;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 【前台】用户浏览历史管理
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/browsing/history")
public class FdBrowsingHistoryController extends BaseController {

    private final IBrowsingHistoryService iBrowsingHistoryService;

    /**
     * 查询用户浏览历史列表
     */
    @GetMapping("/list")
    public TableDataInfo<BrowsingHistoryVo> list(@Validated(QueryGroup.class) BrowsingHistoryBo bo, PageQuery pageQuery) {
        return iBrowsingHistoryService.queryPageUidList(bo, pageQuery);
    }

    /**
     * 获取用户浏览历史详细信息
     */
    @GetMapping("/{id}")
    public R<BrowsingHistoryVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iBrowsingHistoryService.queryById(id));
    }

    /**
     * 新增用户浏览历史
     */
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody BrowsingHistoryBo bo) {
        return toAjax(iBrowsingHistoryService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户浏览历史
     */
    @SaCheckPermission("browsing:history:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iBrowsingHistoryService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }

}
