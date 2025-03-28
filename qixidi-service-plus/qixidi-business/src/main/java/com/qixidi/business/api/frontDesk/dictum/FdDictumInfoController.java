package com.qixidi.business.api.frontDesk.dictum;


import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.service.dictum.IDictumInfoService;
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
import com.qixidi.auth.helper.LoginHelper;
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
