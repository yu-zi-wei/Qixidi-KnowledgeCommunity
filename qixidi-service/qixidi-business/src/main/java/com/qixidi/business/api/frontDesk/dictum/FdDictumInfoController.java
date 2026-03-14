package com.qixidi.business.api.frontDesk.dictum;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.service.dictum.IDictumInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】名言信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/info")
public class FdDictumInfoController {

    private final IDictumInfoService iDictumInfoService;

    /**
     * 查询名言信息列表(后台)
     */
    @GetMapping("/role/list")
    public TableDataInfo<DictumInfoVo> roleList(@Validated(QueryGroup.class) DictumInfoBo bo, PageQuery pageQuery) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return iDictumInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取名言信息详细信息
     */
    @GetMapping("/{id}")
    public DictumInfoVo getInfo(@PathVariable("id") Long id) {
        return iDictumInfoService.queryById(id);
    }

    /**
     * 新增名言信息
     */
    @Log(title = "新增名言信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody DictumInfoBo bo) {
        if (bo.getId() != null) {
            iDictumInfoService.updateByBo(bo);
        } else {
            iDictumInfoService.insertByBo(bo);
        }
    }

    /**
     * 修改名言信息
     */
    @Log(title = "更新名言信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody DictumInfoBo bo) {
        iDictumInfoService.updateByBo(bo);
    }

    /**
     * 删除名言信息
     */
    @Log(title = "删除名言信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}/{groupId}")
    public void remove(@PathVariable Long id, @PathVariable Long groupId) {
        iDictumInfoService.deleteWithValidById(id, groupId);
    }
}
