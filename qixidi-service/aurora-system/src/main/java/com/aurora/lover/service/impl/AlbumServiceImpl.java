/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:53
 * @version 1.0
 */
package com.aurora.lover.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.Album;
import com.aurora.lover.domain.bo.AlbumBo;
import com.aurora.lover.domain.vo.AlbumVo;
import com.aurora.lover.mapper.AlbumMapper;
import com.aurora.lover.service.IAlbumService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 时光相册Service业务层处理
 *
 * @author ziwei
 * @date 2022-11-20
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class AlbumServiceImpl implements IAlbumService {

    private final AlbumMapper baseMapper;

    /**
     * 查询时光相册
     */
    @Override
    public AlbumVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询时光相册列表
     */
    @Override
    public TableDataInfo<AlbumVo> queryPageList(AlbumBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Album> lqw = buildQueryWrapper(bo);
        Page<AlbumVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询时光相册列表
     */
    @Override
    public List<AlbumVo> queryList(AlbumBo bo) {
        LambdaQueryWrapper<Album> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Album> buildQueryWrapper(AlbumBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Album> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getImg()), Album::getImg, bo.getImg());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), Album::getAddress, bo.getAddress());
        lqw.like(StringUtils.isNotBlank(bo.getRemarks()), Album::getRemarks, bo.getRemarks());
        lqw.eq(bo.getOrder() != null, Album::getOrder, bo.getOrder());
        lqw.eq(bo.getState() != null, Album::getState, bo.getState());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            Album::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(Album::getId);
        return lqw;
    }

    /**
     * 新增时光相册
     */
    @Override
    public Boolean insertByBo(AlbumBo bo) {
        Album add = BeanUtil.toBean(bo, Album.class);
        add.setUpdateTime(new Date());

        add.setCreateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改时光相册
     */
    @Override
    public Boolean updateByBo(AlbumBo bo) {
        Album update = BeanUtil.toBean(bo, Album.class);
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除时光相册
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<AlbumVo> queryPageListApi(AlbumBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Album> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), Album::getAddress, bo.getAddress());
        lqw.eq(true, Album::getState, bo.getState());
        lqw.orderByDesc(Album::getCreateTime);
        Page<AlbumVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
}
