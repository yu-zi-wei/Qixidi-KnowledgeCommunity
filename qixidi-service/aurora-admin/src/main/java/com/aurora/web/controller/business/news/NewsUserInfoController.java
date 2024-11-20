package com.aurora.web.controller.business.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.news.NewsUserInfoBo;
import com.aurora.business.domain.vo.news.NewsUserInfoVo;
import com.aurora.business.service.news.INewsUserInfoService;
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
 * 用户消息Controller
 *
 * @author aurora
 * @date 2022-11-03
 */
@Validated
@Api(value = "用户消息控制器", tags = {"用户消息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/news")
public class NewsUserInfoController extends BaseController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息列表
     */
    @ApiOperation("查询用户消息列表")
    @SaCheckPermission("news:userInfo:list")
    @GetMapping("/list")
    public TableDataInfo<NewsUserInfoVo> list(@Validated(QueryGroup.class) NewsUserInfoBo bo, PageQuery pageQuery) {
        return iNewsUserInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户消息列表
     */
    @ApiOperation("导出用户消息列表")
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
    @ApiOperation("获取用户消息详细信息")
    @SaCheckPermission("news:userInfo:query")
    @GetMapping("/{id}")
    public R<NewsUserInfoVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(iNewsUserInfoService.queryById(id));
    }

    /**
     * 新增用户消息
     */
    @ApiOperation("新增用户消息")
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
    @ApiOperation("修改用户消息")
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
    @ApiOperation("删除用户消息")
    @SaCheckPermission("news:userInfo:remove")
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
