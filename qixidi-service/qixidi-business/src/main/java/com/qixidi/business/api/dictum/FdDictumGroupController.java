package com.qixidi.business.api.dictum;


import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.qixidi.business.service.dictum.IDictumGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 【前台】随笔分组管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/group")
@LogModule("随笔分组")
public class FdDictumGroupController {

    private final IDictumGroupService iDictumGroupService;

    /**
     * 获取随笔分组详细信息
     */
    @GetMapping("/{id}")
    public DictumGroupVo getInfo(@PathVariable("id") Long id) {
        return iDictumGroupService.queryById(id);
    }

    /**
     * 新增随笔分组
     */
    @Log(title = "新增随笔分组", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody DictumGroupBo bo) {
        if (!iDictumGroupService.insertByBo(bo)) throw new ServiceException("新增随笔分组失败");
    }

    /**
     * 修改随笔分组
     */
    @Log(title = "修改随笔分组", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody DictumGroupBo bo) {
        if (!iDictumGroupService.updateByBo(bo)) throw new ServiceException("更新随笔分组失败");
    }

    /**
     * 删除随笔分组
     */
    @Log(title = "删除随笔分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        if (!iDictumGroupService.deleteWithValidByIds(Arrays.asList(ids), true))
            throw new ServiceException("删除随笔分组失败");
    }
}
