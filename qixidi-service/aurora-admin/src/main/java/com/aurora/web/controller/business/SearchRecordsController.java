package com.aurora.web.controller.business;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.SearchRecordsBo;
import com.aurora.business.domain.vo.SearchRecordsVo;
import com.aurora.business.service.ISearchRecordsService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    public R<SearchRecordsVo> getInfo(@ApiParam("主键")
                                      @NotNull(message = "主键不能为空")
                                      @PathVariable("id") Long id) {
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
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSearchRecordsService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

