package com.qixidi.business.comtroller.backstage.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.service.news.INewsUserInfoService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
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
@RequestMapping("/business/news")
public class NewsUserInfoController extends BaseController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息列表
     */
    @SaCheckPermission("news:userInfo:list")
    @GetMapping("/list")
    public TableDataInfo<NewsUserInfoVo> list(@Validated(QueryGroup.class) NewsUserInfoBo bo, PageQuery pageQuery) {
        return iNewsUserInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户消息列表
     */
    @SaCheckPermission("news:userInfo:export")
    @Log(title = "用户消息", businessType = BusinessType.EXPORT)
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
    public R<NewsUserInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iNewsUserInfoService.queryById(id));
    }

    /**
     * 新增用户消息
     */
    @SaCheckPermission("news:userInfo:add")
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NewsUserInfoBo bo) {
        return toAjax(iNewsUserInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户消息
     */
    @SaCheckPermission("news:userInfo:edit")
    @Log(title = "用户消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NewsUserInfoBo bo) {
        return toAjax(iNewsUserInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户消息
     */
    @SaCheckPermission("news:userInfo:remove")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
