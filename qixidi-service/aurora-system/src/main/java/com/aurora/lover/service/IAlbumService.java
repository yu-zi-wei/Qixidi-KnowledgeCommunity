/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:52
 * @version 1.0
 */
package com.aurora.lover.service;


import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.AlbumBo;
import com.aurora.lover.domain.vo.AlbumVo;

import java.util.Collection;
import java.util.List;

/**
 * 时光相册Service接口
 *
 * @author ziwei
 * @date 2022-11-20
 */
public interface IAlbumService {

    /**
     * 查询时光相册
     */
    AlbumVo queryById(Long id);

    /**
     * 查询时光相册列表
     */
    TableDataInfo<AlbumVo> queryPageList(AlbumBo bo, PageQuery pageQuery);

    /**
     * 查询时光相册列表
     */
    List<AlbumVo> queryList(AlbumBo bo);

    /**
     * 新增时光相册
     */
    Boolean insertByBo(AlbumBo bo);

    /**
     * 修改时光相册
     */
    Boolean updateByBo(AlbumBo bo);

    /**
     * 校验并批量删除时光相册信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<AlbumVo> queryPageListApi(AlbumBo bo, PageQuery pageQuery);
}
