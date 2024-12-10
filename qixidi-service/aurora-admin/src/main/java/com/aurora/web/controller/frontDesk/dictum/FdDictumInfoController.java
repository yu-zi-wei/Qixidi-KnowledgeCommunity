package com.aurora.web.controller.frontDesk.dictum;


import com.aurora.business.domain.bo.dictum.DictumInfoBo;
import com.aurora.business.domain.vo.dictum.DictumInfoVo;
import com.aurora.business.service.dictum.IDictumInfoService;
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
import com.aurora.common.helper.LoginHelper;
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
public class FdDictumInfoController extends BaseController {

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
    public R<DictumInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumInfoService.queryById(id));
    }

    /**
     * 新增名言信息
     */
    @Log(title = "新增名言信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumInfoBo bo) {
        if (bo.getId() != null) {
            return toAjax(iDictumInfoService.updateByBo(bo) ? 1 : 0);
        } else {
            return toAjax(iDictumInfoService.insertByBo(bo) ? 1 : 0);
        }
    }

    /**
     * 修改名言信息
     */
    @Log(title = "更新名言信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumInfoBo bo) {
        return toAjax(iDictumInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言信息
     */
    @Log(title = "删除名言信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}/{groupId}")
    public R<Void> remove(@PathVariable Long id, @PathVariable Long groupId) {
        return toAjax(iDictumInfoService.deleteWithValidById(id, groupId) ? 1 : 0);
    }
}
