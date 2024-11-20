/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:18
 * @version 1.0
 */
package com.aurora.lover.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.LoverCarousel;
import com.aurora.lover.domain.bo.LoverCarouselBo;
import com.aurora.lover.domain.vo.LoverCarouselVo;
import com.aurora.lover.mapper.LoverCarouselMapper;
import com.aurora.lover.service.ILoverCarouselService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 爱情轮播图Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-29
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class LoverCarouselServiceImpl implements ILoverCarouselService {

    private final LoverCarouselMapper baseMapper;

    /**
     * 查询爱情轮播图
     */
    @Override
    public LoverCarouselVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询爱情轮播图列表
     */
    @Override
    public TableDataInfo<LoverCarouselVo> queryPageList(LoverCarouselBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverCarousel> lqw = buildQueryWrapper(bo);
        Page<LoverCarouselVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询爱情轮播图列表
     */
    @Override
    public List<LoverCarouselVo> queryList(LoverCarouselBo bo) {
        LambdaQueryWrapper<LoverCarousel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LoverCarousel> buildQueryWrapper(LoverCarouselBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LoverCarousel> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getImg()), LoverCarousel::getImg, bo.getImg());
        lqw.eq(StringUtils.isNotBlank(bo.getImgId()), LoverCarousel::getImgId, bo.getImgId());
        lqw.like(StringUtils.isNotBlank(bo.getIntroduce()), LoverCarousel::getIntroduce, bo.getIntroduce());
        lqw.eq(bo.getType() != null, LoverCarousel::getType, bo.getType());
        lqw.eq(bo.getState() != null, LoverCarousel::getState, bo.getState());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            LoverCarousel::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(LoverCarousel::getId);
        return lqw;
    }

    /**
     * 新增爱情轮播图
     */
    @Override
    public Boolean insertByBo(LoverCarouselBo bo) {
        LoverCarousel add = BeanUtil.toBean(bo, LoverCarousel.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改爱情轮播图
     */
    @Override
    public Boolean updateByBo(LoverCarouselBo bo) {
        LoverCarousel update = BeanUtil.toBean(bo, LoverCarousel.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除爱情轮播图
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<LoverCarouselVo> carouselApi(LoverCarouselBo bo, PageQuery pageQuery) {
        return baseMapper.carouselApi(pageQuery.build().getSize());
    }
}
