package com.aurora.business.service.impl.dictum;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.dictum.DictumAlbumBo;
import com.aurora.business.domain.entity.dictum.DictumAlbum;
import com.aurora.business.domain.entity.dictum.DictumInfo;
import com.aurora.business.domain.vo.dictum.DictumAlbumVo;
import com.aurora.business.mapper.dictum.DictumAlbumMapper;
import com.aurora.business.mapper.dictum.DictumInfoMapper;
import com.aurora.business.service.dictum.IDictumAlbumService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 名言专辑Service业务层处理
 *
 * @author aurora
 * @date 2023-04-24
 */
@RequiredArgsConstructor
@Service
public class DictumAlbumServiceImpl implements IDictumAlbumService {

    private final DictumAlbumMapper baseMapper;
    private final DictumInfoMapper dictumInfoMapper;

    /**
     * 查询名言专辑
     *
     * @param id 名言专辑主键
     * @return 名言专辑
     */
    @Override
    public DictumAlbumVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询名言专辑列表
     *
     * @param bo 名言专辑
     * @return 名言专辑
     */
    @Override
    public TableDataInfo<DictumAlbumVo> queryPageList(DictumAlbumBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DictumAlbum> lqw = buildQueryWrapper(bo);
        IPage<DictumAlbumVo> result = baseMapper.selectVoPageXml(bo, pageQuery.build());
        return TableDataInfo.build(result);
    }

    /**
     * 查询名言专辑列表
     *
     * @param bo 名言专辑
     * @return 名言专辑
     */
    @Override
    public List<DictumAlbumVo> queryList(DictumAlbumBo bo) {
        LambdaQueryWrapper<DictumAlbum> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DictumAlbum> buildQueryWrapper(DictumAlbumBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DictumAlbum> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), DictumAlbum::getUid, bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DictumAlbum::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCover()), DictumAlbum::getCover, bo.getCover());
        lqw.eq(StringUtils.isNotBlank(bo.getBriefIntroduction()), DictumAlbum::getBriefIntroduction, bo.getBriefIntroduction());
        lqw.eq(bo.getAlbumState() != null, DictumAlbum::getAlbumState, bo.getAlbumState());
        lqw.eq(bo.getHelpSum() != null, DictumAlbum::getHelpSum, bo.getHelpSum());
        lqw.eq(bo.getFollowSum() != null, DictumAlbum::getFollowSum, bo.getFollowSum());
        lqw.eq(bo.getState() != null, DictumAlbum::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增名言专辑
     *
     * @param bo 名言专辑
     * @return 结果
     */
    @Override
    public Boolean insertByBo(DictumAlbumBo bo) {
        DictumAlbum add = BeanUtil.toBean(bo, DictumAlbum.class);
        add.setUid(LoginHelper.getTripartiteUuid());
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改名言专辑
     *
     * @param bo 名言专辑
     * @return 结果
     */
    @Override
    public Boolean updateByBo(DictumAlbumBo bo) {
        DictumAlbum update = BeanUtil.toBean(bo, DictumAlbum.class);
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除名言专辑
     *
     * @param ids 需要删除的名言专辑主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean deleteWithValidById(Long id) throws Exception {
        Long selectCount = dictumInfoMapper.selectCount(new QueryWrapper<DictumInfo>().eq("album_id", id));
        if (selectCount > 0) {
            throw new Exception("当前专辑存在名言，不可删除！");
        }
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<DictumAlbumVo> recommendedAlbum() {
        IPage iPage = baseMapper.selectVoPage(new Page(0, 9),
            new QueryWrapper<DictumAlbum>().orderByDesc("employ_sum").lambda());
        return iPage.getRecords();
    }

}

