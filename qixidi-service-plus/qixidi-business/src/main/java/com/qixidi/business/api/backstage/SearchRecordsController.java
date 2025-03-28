package com.qixidi.business.api.backstage;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.SearchRecordsBo;
import com.qixidi.business.domain.vo.SearchRecordsVo;
import com.qixidi.business.service.ISearchRecordsService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
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
 * 搜索记录管理
 *
 * @author aurora
 * @date 2023-04-18
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/search/records")
public class SearchRecordsController extends BaseController {

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
    public R<SearchRecordsVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iSearchRecordsService.queryById(id));
    }

    /**
     * 新增搜索记录
     */
    @SaCheckPermission("business:search:records:add")
    @Log(title = "搜索记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SearchRecordsBo bo) {
        return toAjax(iSearchRecordsService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改搜索记录
     */
    @SaCheckPermission("business:search:records:edit")
    @Log(title = "搜索记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SearchRecordsBo bo) {
        return toAjax(iSearchRecordsService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除搜索记录
     */
    @SaCheckPermission("business:search:records:remove")
    @Log(title = "搜索记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iSearchRecordsService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

