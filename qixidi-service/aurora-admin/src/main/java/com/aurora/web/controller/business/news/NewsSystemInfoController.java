package com.aurora.web.controller.business.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.news.NewsSystemInfoBo;
import com.aurora.business.domain.vo.news.NewsSystemInfoVo;
import com.aurora.business.service.news.INewsSystemInfoService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 系统消息Controller
 *
 * @author aurora
 * @date 2023-04-23
 */
@Validated
@Api(value = "系统消息控制器", tags = {"系统消息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/news/systemInfo")
public class NewsSystemInfoController extends BaseController {

    private final INewsSystemInfoService iNewsSystemInfoService;

    /**
     * 查询系统消息列表
     */
    @ApiOperation("查询系统消息列表")
    @SaCheckPermission("business:news:systemInfo:list")
    @GetMapping("/list")
    public TableDataInfo<NewsSystemInfoVo> list(@Validated(QueryGroup.class) NewsSystemInfoBo bo, PageQuery pageQuery) {
        return iNewsSystemInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出系统消息列表
     */
    @ApiOperation("导出系统消息列表")
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
    @ApiOperation("获取系统消息详细信息")
    @SaCheckPermission("business:news:systemInfo:query")
    @GetMapping("/{id}")
    public R<NewsSystemInfoVo> getInfo(@ApiParam("主键")
                                       @NotNull(message = "主键不能为空")
                                       @PathVariable("id") Long id) {
        return R.ok(iNewsSystemInfoService.queryById(id));
    }

    /**
     * 新增系统消息
     */
    @ApiOperation("新增系统消息")
    @SaCheckPermission("business:news:systemInfo:add")
    @Log(title = "系统消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody NewsSystemInfoBo bo) {
        return toAjax(iNewsSystemInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改系统消息
     */
    @ApiOperation("修改系统消息")
    @SaCheckPermission("business:news:systemInfo:edit")
    @Log(title = "系统消息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody NewsSystemInfoBo bo) {
        return toAjax(iNewsSystemInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除系统消息
     */
    @ApiOperation("删除系统消息")
    @SaCheckPermission("business:news:systemInfo:remove")
    @Log(title = "系统消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iNewsSystemInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

