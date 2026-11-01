package com.qixidi.system.api.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.PermName;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.FriendLinkBo;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.qixidi.business.service.IFriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 友链管理
 *
 * @author ziwei
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/friendLink")
@LogModule("友链")
public class FriendLinkAdminController {

    private final IFriendLinkService friendLinkService;

    /**
     * 友链分页列表（名称模糊 + 状态多选筛选）
     */
    @PermName("友情链接-查询")
    @SaCheckPermission("friendLink:query")
    @PostMapping("/list")
    public TableDataInfo<FriendLinkVo> list(@Validated(QueryGroup.class) @RequestBody FriendLinkBo bo) {
        return friendLinkService.queryPageList(bo);
    }

    /**
     * 友链详情
     */
    @PermName("友情链接-查询")
    @SaCheckPermission("friendLink:query")
    @GetMapping("/{id}")
    public FriendLinkVo getInfo(@PathVariable("id") Long id) {
        return friendLinkService.queryById(id);
    }

    /**
     * 新增友链
     */
    @PermName("友情链接-新增")
    @SaCheckPermission("friendLink:add")
    @Log(title = "新增友链", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody FriendLinkBo bo) {
        friendLinkService.insertByBo(bo);
    }

    /**
     * 修改友链
     */
    @PermName("友情链接-编辑")
    @SaCheckPermission("friendLink:edit")
    @Log(title = "修改友链", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody FriendLinkBo bo) {
        friendLinkService.updateByBo(bo);
    }

    /**
     * 删除友链
     */
    @PermName("友情链接-删除")
    @SaCheckPermission("friendLink:remove")
    @Log(title = "删除友链", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        friendLinkService.deleteByIds(Arrays.asList(ids));
    }
}
