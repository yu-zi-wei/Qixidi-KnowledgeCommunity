package com.aurora.web.controller.frontDesk.dictum;


import com.aurora.business.domain.bo.dictum.DictumGroupBo;
import com.aurora.business.domain.vo.dictum.DictumGroupVo;
import com.aurora.business.service.dictum.IDictumGroupService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Validated
@Api(value = "名言分组控制器", tags = {"名言分组管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/group")
public class FdDictumGroupController extends BaseController {

    private final IDictumGroupService iDictumGroupService;

    /**
     * 获取名言分组详细信息
     */
    @ApiOperation("获取名言分组详细信息")
    @GetMapping("/{id}")
    public R<DictumGroupVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("id") Long id) {
        return R.ok(iDictumGroupService.queryById(id));
    }

    /**
     * 新增名言分组
     */
    @ApiOperation("新增名言分组")
    @Log(title = "名言分组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumGroupBo bo) {
        return toAjax(iDictumGroupService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改名言分组
     */
    @ApiOperation("修改名言分组")
    @Log(title = "名言分组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumGroupBo bo) {
        return toAjax(iDictumGroupService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言分组
     */
    @ApiOperation("删除名言分组")
    @Log(title = "名言分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iDictumGroupService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
