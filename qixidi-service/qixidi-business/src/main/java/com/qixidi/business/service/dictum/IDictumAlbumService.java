package com.qixidi.business.service.dictum;

import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 名言专辑Service接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface IDictumAlbumService {

    /**
     * 查询名言专辑
     *
     * @param id 名言专辑主键
     * @return 名言专辑
     */
    DictumAlbumVo queryById(Long id);

    /**
     * 查询名言专辑列表
     *
     * @param bo 名言专辑
     * @return 名言专辑集合
     */
    TableDataInfo<DictumAlbumVo> queryPageList(DictumAlbumBo bo, PageQuery pageQuery);

    /**
     * 查询名言专辑列表
     *
     * @param bo 名言专辑
     * @return 名言专辑集合
     */
    List<DictumAlbumVo> queryList(DictumAlbumBo bo);

    /**
     * 修改名言专辑
     *
     * @param bo 名言专辑
     * @return 结果
     */
    Boolean insertByBo(DictumAlbumBo bo);

    /**
     * 修改名言专辑
     *
     * @param bo 名言专辑
     * @return 结果
     */
    Boolean updateByBo(DictumAlbumBo bo);

    /**
     * 校验并批量删除名言专辑信息
     *
     * @param ids     需要删除的名言专辑主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean deleteWithValidById(Long id) throws Exception;

    List<DictumAlbumVo> recommendedAlbum();

}

