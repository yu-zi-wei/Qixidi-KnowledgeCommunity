package com.qixidi.business.api.frontDesk.album;

import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.album.AlbumSearchBo;
import com.qixidi.business.domain.vo.album.AlbumMonthVo;
import com.qixidi.business.domain.vo.album.AlbumVo;
import com.qixidi.business.service.album.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【前台白名单】相册
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/album")
public class AlbumWhiteController {

    private final IAlbumService albumService;

    /**
     * 相册图片分页（免登录，全站图片墙，可选 uid 筛选）
     */
    @PostMapping("/list")
    public TableDataInfo<AlbumVo> list(@RequestBody AlbumSearchBo bo) {
        return albumService.list(bo);
    }

    /**
     * 月份统计（yyyy-MM + 张数，倒序），目录用，与图片分页解耦
     */
    @GetMapping("/months")
    public List<AlbumMonthVo> months() {
        return albumService.listMonths();
    }

}
