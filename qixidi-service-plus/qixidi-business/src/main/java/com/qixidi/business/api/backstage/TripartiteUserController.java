package com.qixidi.business.api.backstage;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.api.BaseController;
import com.qixidi.business.domain.bo.user.TripartiteUserBo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.service.ITripartiteUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台用户管理管理
 *
 * @author ziwei
 * @date 2022-06-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/tripartite/user")
public class TripartiteUserController extends BaseController {

    private final ITripartiteUserService iTripartiteUserService;

    /**
     * 查询平台用户列表
     */
    @SaCheckPermission("system:main:list")
    @GetMapping("/list")
    public TableDataInfo<TripartiteUserVo> list(@Validated(QueryGroup.class) TripartiteUserBo bo, PageQuery pageQuery) {
        return iTripartiteUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出平台用户列表
     */
    @SaCheckPermission("system:main:export")
    @Log(title = "平台用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TripartiteUserBo bo, HttpServletResponse response) {
        List<TripartiteUserVo> list = iTripartiteUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "平台用户", TripartiteUserVo.class, response);
    }

    /**
     * 获取平台用户详细信息
     */
    @SaCheckPermission("system:main:query")
    @GetMapping("/{uuid}")
    public R<TripartiteUserVo> getInfo(@PathVariable("uuid") String uuid) {
        return R.ok(iTripartiteUserService.queryById(uuid));
    }

    /**
     * 修改平台用户
     */
    @SaCheckPermission("system:main:edit")
    @Log(title = "更新平台用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TripartiteUserBo bo) {
        return toAjax(iTripartiteUserService.updateByBo(bo) ? 1 : 0);
    }

}
