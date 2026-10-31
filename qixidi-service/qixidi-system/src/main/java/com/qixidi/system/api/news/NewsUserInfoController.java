package com.qixidi.system.api.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.service.news.INewsUserInfoService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 用户消息管理
 *
 * @author aurora
 * @date 2022-11-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/news")
@LogModule("用户消息")
public class NewsUserInfoController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息列表
     */
    @SaCheckPermission("news:userInfo:list")
    @PostMapping("/list")
    public TableDataInfo<NewsUserInfoVo> list(@Validated(QueryGroup.class) @RequestBody NewsUserInfoBo bo) {
        return iNewsUserInfoService.queryPageList(bo, bo);
    }

    /**
     * 导出用户消息列表
     */
    @SaCheckPermission("news:userInfo:export")
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated NewsUserInfoBo bo, HttpServletResponse response) {
        List<NewsUserInfoVo> list = iNewsUserInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户消息", NewsUserInfoVo.class, response);
    }

    /**
     * 获取用户消息详细信息
     */
    @SaCheckPermission("news:userInfo:query")
    @GetMapping("/{id}")
    public NewsUserInfoVo getInfo(@PathVariable("id") Long id) {
        return iNewsUserInfoService.queryById(id);
    }

    /**
     * 新增用户消息
     */
    @SaCheckPermission("news:userInfo:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody NewsUserInfoBo bo) {
        iNewsUserInfoService.insertByBo(bo);
    }

    /**
     * 修改用户消息
     */
    @SaCheckPermission("news:userInfo:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody NewsUserInfoBo bo) {
        iNewsUserInfoService.updateByBo(bo);
    }

    /**
     * 删除用户消息
     */
    @SaCheckPermission("news:userInfo:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
