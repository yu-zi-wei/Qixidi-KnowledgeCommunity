package com.qixidi.business.api.backstage.privateUser;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.service.privateUser.IPrivateUserService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 私信记录管理
 *
 * @author aurora
 * @date 2023-03-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/private/user")
public class PrivateUserController {

    private final IPrivateUserService iPrivateUserService;

    /**
     * 查询私信记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<PrivateUserVo> list(@Validated(QueryGroup.class) PrivateUserBo bo, PageQuery pageQuery) {
        return iPrivateUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出私信记录列表
     */
    @Log(title = "私信记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated PrivateUserBo bo, HttpServletResponse response) {
        List<PrivateUserVo> list = iPrivateUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "私信记录", PrivateUserVo.class, response);
    }

    /**
     * 获取私信记录详细信息
     */
    @GetMapping("/{id}")
    public PrivateUserVo getInfo(@PathVariable("id") Long id) {
        return iPrivateUserService.queryById(id);
    }

    /**
     * 新增私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody PrivateUserBo bo) {
        iPrivateUserService.insertByBo(bo);
    }

    /**
     * 修改私信记录
     */
    @Log(title = "私信记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody PrivateUserBo bo) {
        iPrivateUserService.updateByBo(bo);
    }

    /**
     * 删除私信记录
     */
    @SaCheckPermission("private:user:remove")
    @Log(title = "私信记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iPrivateUserService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

