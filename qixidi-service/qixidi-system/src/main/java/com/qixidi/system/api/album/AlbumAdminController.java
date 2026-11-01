package com.qixidi.system.api.album;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.PermName;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.album.AlbumSearchBo;
import com.qixidi.business.domain.vo.album.AlbumVo;
import com.qixidi.business.service.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 【后台】时光相册管理
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/album")
@LogModule("时光相册")
public class AlbumAdminController {

    private final IAlbumService albumService;

    /**
     * 相册图片分页（归属用户昵称/头像 join 展示，支持 uid/来源/月份筛选）
     */
    @PermName("时光相册-列表")
    @SaCheckPermission("album:query")
    @PostMapping("/list")
    public TableDataInfo<AlbumVo> list(@RequestBody AlbumSearchBo bo) {
        return albumService.list(bo);
    }

    /**
     * 批量删除相册图片（移出相册；来源内容再编辑/回填时会按内容重新解析入库）
     */
    @PermName("时光相册-删除")
    @SaCheckPermission("album:remove")
    @Log(title = "删除相册图片", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        albumService.adminDelete(Arrays.asList(ids));
    }

    /**
     * 一次性回填：全量解析时光小记内容中的图片入库相册
     * （可重复触发，按小记全删重插，回填数量见服务端日志）
     */
    @Log(title = "相册回填", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/backfill")
    public void backfill() {
        albumService.backfillTimeNotes();
    }
}
