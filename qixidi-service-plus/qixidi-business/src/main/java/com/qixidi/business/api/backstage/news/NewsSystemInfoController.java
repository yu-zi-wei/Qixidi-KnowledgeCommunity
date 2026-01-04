package com.qixidi.business.api.backstage.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.news.NewsSystemInfoBo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.service.news.INewsSystemInfoService;
import com.qixidi.auth.annotation.Log;
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
 * 系统消息管理
 *
 * @author aurora
 * @date 2023-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/news/systemInfo")
public class NewsSystemInfoController {

    private final INewsSystemInfoService iNewsSystemInfoService;

    /**
     * 查询系统消息列表
     */
    @SaCheckPermission("business:news:systemInfo:list")
    @GetMapping("/list")
    public TableDataInfo<NewsSystemInfoVo> list(@Validated(QueryGroup.class) NewsSystemInfoBo bo, PageQuery pageQuery) {
        return iNewsSystemInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出系统消息列表
     */
    @SaCheckPermission("business:news:systemInfo:export")
    @Log(title = "系统消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated NewsSystemInfoBo bo, HttpServletResponse response) {
        List<NewsSystemInfoVo> list = iNewsSystemInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "系统消息", NewsSystemInfoVo.class, response);
    }

    /**
     * 获取系统消息详细信息
     */
    @SaCheckPermission("business:news:systemInfo:query")
    @GetMapping("/{id}")
    public NewsSystemInfoVo getInfo(@PathVariable("id") Long id) {
        return iNewsSystemInfoService.queryById(id);
    }

    /**
     * 新增系统消息
     */
    @SaCheckPermission("business:news:systemInfo:add")
    @Log(title = "系统消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody NewsSystemInfoBo bo) {
        iNewsSystemInfoService.insertByBo(bo);
    }

    /**
     * 修改系统消息
     */
    @SaCheckPermission("business:news:systemInfo:edit")
    @Log(title = "系统消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody NewsSystemInfoBo bo) {
        iNewsSystemInfoService.updateByBo(bo);
    }

    /**
     * 删除系统消息
     */
    @SaCheckPermission("business:news:systemInfo:remove")
    @Log(title = "系统消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iNewsSystemInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

