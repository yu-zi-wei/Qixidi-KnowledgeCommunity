package com.qixidi.business.api.frontDesk.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.user.BrowsingHistoryBo;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.qixidi.business.service.IBrowsingHistoryService;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.QueryGroup;
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
public class FdBrowsingHistoryController {

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
    public BrowsingHistoryVo getInfo(@PathVariable("id") Long id) {
        return iBrowsingHistoryService.queryById(id);
    }

    /**
     * 新增用户浏览历史
     */
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody BrowsingHistoryBo bo) {
        iBrowsingHistoryService.insertByBo(bo);
    }

    /**
     * 删除用户浏览历史
     */
    @SaCheckPermission("browsing:history:remove")
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iBrowsingHistoryService.deleteWithValidByIds(Arrays.asList(ids), true);
    }

}
