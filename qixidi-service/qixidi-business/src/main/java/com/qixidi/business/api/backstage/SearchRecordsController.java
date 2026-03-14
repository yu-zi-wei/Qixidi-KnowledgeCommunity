package com.qixidi.business.api.backstage;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.SearchRecordsBo;
import com.qixidi.business.domain.vo.SearchRecordsVo;
import com.qixidi.business.service.ISearchRecordsService;
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
 * 搜索记录管理
 *
 * @author aurora
 * @date 2023-04-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/search/records")
public class SearchRecordsController {

    private final ISearchRecordsService iSearchRecordsService;

    /**
     * 查询搜索记录列表
     */
    @SaCheckPermission("business:search:records:list")
    @GetMapping("/list")
    public TableDataInfo<SearchRecordsVo> list(@Validated(QueryGroup.class) SearchRecordsBo bo, PageQuery pageQuery) {
        return iSearchRecordsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出搜索记录列表
     */
    @SaCheckPermission("business:search:records:export")
    @Log(title = "搜索记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated SearchRecordsBo bo, HttpServletResponse response) {
        List<SearchRecordsVo> list = iSearchRecordsService.queryList(bo);
        ExcelUtil.exportExcel(list, "搜索记录", SearchRecordsVo.class, response);
    }

    /**
     * 获取搜索记录详细信息
     */
    @SaCheckPermission("business:search:records:query")
    @GetMapping("/{id}")
    public SearchRecordsVo getInfo(@PathVariable("id") Long id) {
        return iSearchRecordsService.queryById(id);
    }

    /**
     * 新增搜索记录
     */
    @SaCheckPermission("business:search:records:add")
    @Log(title = "搜索记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody SearchRecordsBo bo) {
        iSearchRecordsService.insertByBo(bo);
    }

    /**
     * 修改搜索记录
     */
    @SaCheckPermission("business:search:records:edit")
    @Log(title = "搜索记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody SearchRecordsBo bo) {
        iSearchRecordsService.updateByBo(bo);
    }

    /**
     * 删除搜索记录
     */
    @SaCheckPermission("business:search:records:remove")
    @Log(title = "搜索记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iSearchRecordsService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

