package com.qixidi.business.api.backstage.shield;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.shield.ToShieldWordBo;
import com.qixidi.business.domain.vo.shield.ToShieldWordVo;
import com.qixidi.business.service.shield.IToShieldWordService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 屏蔽池库管理
 *
 * @author aurora
 * @date 2023-02-20
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/shield/shieldWord")
public class ToShieldWordController {
    private final IToShieldWordService iToShieldWordService;

    /**
     * 查询屏蔽池库列表
     */
    @SaCheckPermission("shield:shieldWord:list")
    @GetMapping("/list")
    public TableDataInfo<ToShieldWordVo> list(@Validated(QueryGroup.class) ToShieldWordBo bo, PageQuery pageQuery) {

        return iToShieldWordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出屏蔽词库列表
     */
    @SaCheckPermission("shield:shieldWord:export")
    @Log(title = "屏蔽池库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToShieldWordBo bo, HttpServletResponse response) {
        List<ToShieldWordVo> list = iToShieldWordService.queryList(bo);
        ExcelUtil.exportExcel(list, "屏蔽词库", ToShieldWordVo.class, response);
    }

    /**
     * 获取屏蔽词库详细信息
     */
    @SaCheckPermission("shield:shieldWord:query")
    @GetMapping("/{id}")
    public ToShieldWordVo getInfo(@PathVariable("id") Long id) {
        return iToShieldWordService.queryById(id);
    }

    /**
     * 新增屏蔽词库
     */
    @SaCheckPermission("shield:shieldWord:add")
    @Log(title = "屏蔽池库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody ToShieldWordBo bo) {
        iToShieldWordService.insertByBo(bo);
    }

    /**
     * 修改屏蔽池库
     */
    @SaCheckPermission("shield:shieldWord:edit")
    @Log(title = "屏蔽池库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ToShieldWordBo bo) {
        iToShieldWordService.updateByBo(bo);
    }

    /**
     * 删除屏蔽词库
     */
    @SaCheckPermission("shield:shieldWord:remove")
    @Log(title = "屏蔽池库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iToShieldWordService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
