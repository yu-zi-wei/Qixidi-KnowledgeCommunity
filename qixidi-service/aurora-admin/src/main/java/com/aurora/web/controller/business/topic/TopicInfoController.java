package com.aurora.web.controller.business.topic;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.topic.TopicInfoBo;
import com.aurora.business.domain.vo.topic.TopicInfoVo;
import com.aurora.business.service.topic.ITopicInfoService;
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
 * 话题信息Controller
 *
 * @author aurora
 * @date 2023-03-09
 */
@Validated
@Api(value = "话题信息控制器", tags = {"话题信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/topic/info")
public class TopicInfoController extends BaseController {

    private final ITopicInfoService iTopicInfoService;

    /**
     * 查询话题信息列表
     */
    @ApiOperation("查询话题信息列表")
    @SaCheckPermission("topic:info:list")
    @GetMapping("/list")
    public TableDataInfo<TopicInfoVo> list(@Validated(QueryGroup.class) TopicInfoBo bo, PageQuery pageQuery) {
        return iTopicInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出话题信息列表
     */
    @ApiOperation("导出话题信息列表")
    @SaCheckPermission("topic:info:export")
    @Log(title = "话题信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TopicInfoBo bo, HttpServletResponse response) {
        List<TopicInfoVo> list = iTopicInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "话题信息", TopicInfoVo.class, response);
    }

    /**
     * 获取话题信息详细信息
     */
    @ApiOperation("获取话题信息详细信息")
    @SaCheckPermission("topic:info:query")
    @GetMapping("/{id}")
    public R<TopicInfoVo> getInfo(@ApiParam("主键")
                                   @NotNull(message = "主键不能为空")
                                   @PathVariable("id") Long id) {
        return R.ok(iTopicInfoService.queryById(id));
    }

    /**
     * 新增话题信息
     */
    @ApiOperation("新增话题信息")
    @SaCheckPermission("topic:info:add")
    @Log(title = "话题信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TopicInfoBo bo) {
        return toAjax(iTopicInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改话题信息
     */
    @ApiOperation("修改话题信息")
    @SaCheckPermission("topic:info:edit")
    @Log(title = "话题信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TopicInfoBo bo) {
        return toAjax(iTopicInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除话题信息
     */
    @ApiOperation("删除话题信息")
    @SaCheckPermission("topic:info:remove")
    @Log(title = "话题信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iTopicInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
