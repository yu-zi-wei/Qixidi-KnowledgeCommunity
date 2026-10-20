package com.qixidi.business.service.album;

import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.album.AlbumSearchBo;
import com.qixidi.business.domain.vo.album.AlbumMonthVo;
import com.qixidi.business.domain.vo.album.AlbumVo;

import java.time.LocalDate;
import java.util.List;

/**
 * 相册（图片来源于时光小记/文章内容中自动解析的图片）
 *
 * @author zi-wei
 */
public interface IAlbumService {

    /**
     * 来源：时光小记
     */
    int SOURCE_TIME_NOTES = 1;

    /**
     * 来源：文章
     */
    int SOURCE_ARTICLE = 2;

    /**
     * 白名单分页查询
     *
     * @param bo uid 可选（按用户），month 可选（yyyy-MM 按月 / yyyy 按年）
     */
    TableDataInfo<AlbumVo> list(AlbumSearchBo bo);

    /**
     * 月份统计（yyyy-MM + 张数，倒序），目录用，不依赖图片分页
     */
    List<AlbumMonthVo> listMonths();

    /**
     * 解析内容图片并同步相册记录（按来源内容全删重插，幂等）
     */
    void syncImages(Integer source, Long sourceId, String uid, LocalDate sourceTime, String content);

    /**
     * 删除来源内容的全部相册记录
     */
    void removeBySource(Integer source, Long sourceId);

    /**
     * 存量回填：全量扫描时光小记内容重建相册，返回入库图片数
     */
    int backfillTimeNotes();
}
