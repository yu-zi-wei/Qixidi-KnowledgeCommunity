package com.qixidi.system.api.album;

import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.service.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【后台】相册维护
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/album")
@LogModule("时光相册")
public class AlbumBackfillController {

    private final IAlbumService albumService;

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
