package com.aurora.web.controller.business.topic;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.topic.TopicJoinInfoBo;
import com.aurora.business.domain.vo.topic.TopicJoinInfoVo;
import com.aurora.business.service.topic.ITopicJoinInfoService;
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
 * 话题参与信息Controller
 *
 * @author aurora
 * @date 2023-03-09
 */
@Validated
@Api(value = "话题参与信息控制器", tags = {"话题参与信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/topicJoin/joinInfo")
public class TopicJoinInfoController extends BaseController {

    private final ITopicJoinInfoService iTopicJoinInfoService;

    /**
     * 查询话题参与信息列表
     */
    @ApiOperation("查询话题参与信息列表")
    @SaCheckPermission("topicJoin:joinInfo:list")
    @GetMapping("/list")
    public TableDataInfo<TopicJoinInfoVo> list(@Validated(QueryGroup.class) TopicJoinInfoBo bo, PageQuery pageQuery) {
        return iTopicJoinInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出话题参与信息列表
     */
    @ApiOperation("导出话题参与信息列表")
    @SaCheckPermission("topicJoin:joinInfo:export")
    @Log(title = "话题参与信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TopicJoinInfoBo bo, HttpServletResponse response) {
        List<TopicJoinInfoVo> list = iTopicJoinInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "话题参与信息", TopicJoinInfoVo.class, response);
    }

    /**
     * 获取话题参与信息详细信息
     */
    @ApiOperation("获取话题参与信息详细信息")
    @SaCheckPermission("topicJoin:joinInfo:query")
    @GetMapping("/{id}")
    public R<TopicJoinInfoVo> getInfo(@ApiParam("主键")
                                      @NotNull(message = "主键不能为空")
                                      @PathVariable("id") Long id) {
        return R.ok(iTopicJoinInfoService.queryById(id));
    }

    /**
     * 新增话题参与信息
     */
    @ApiOperation("新增话题参与信息")
    @SaCheckPermission("topicJoin:joinInfo:add")
    @Log(title = "话题参与信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TopicJoinInfoBo bo) {
        return toAjax(iTopicJoinInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改话题参与信息
     */
    @ApiOperation("修改话题参与信息")
    @SaCheckPermission("topicJoin:joinInfo:edit")
    @Log(title = "话题参与信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TopicJoinInfoBo bo) {
        return toAjax(iTopicJoinInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除话题参与信息
     */
    @ApiOperation("删除话题参与信息")
    @SaCheckPermission("topicJoin:joinInfo:remove")
    @Log(title = "话题参与信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iTopicJoinInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
