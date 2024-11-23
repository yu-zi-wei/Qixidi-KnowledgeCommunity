/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 16:00
 * @version 1.0
 */
package com.aurora.lover.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.Repertoire;
import com.aurora.lover.domain.bo.RepertoireBo;
import com.aurora.lover.domain.vo.RepertoireVo;
import com.aurora.lover.mapper.RepertoireMapper;
import com.aurora.lover.service.IRepertoireService;
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
 * 爱情清单Service业务层处理
 *
 * @author ziwei
 * @date 2022-11-22
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class RepertoireServiceImpl implements IRepertoireService {

    private final RepertoireMapper baseMapper;

    /**
     * 查询爱情清单
     */
    @Override
    public RepertoireVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询爱情清单列表
     */
    @Override
    public TableDataInfo<RepertoireVo> queryPageList(RepertoireBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Repertoire> lqw = buildQueryWrapper(bo);
        //不查询content字段
        lqw.select(Repertoire.class, f -> !f.getColumn().equals("content"));
        Page<RepertoireVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询爱情清单列表
     */
    @Override
    public List<RepertoireVo> queryList(RepertoireBo bo) {
        LambdaQueryWrapper<Repertoire> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Repertoire> buildQueryWrapper(RepertoireBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Repertoire> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Repertoire::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), Repertoire::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getImg()), Repertoire::getImg, bo.getImg());
        lqw.eq(bo.getState() != null, Repertoire::getState, bo.getState());
        lqw.eq(bo.getOrder() != null, Repertoire::getOrder, bo.getOrder());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), Repertoire::getAddress, bo.getAddress());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            Repertoire::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(Repertoire::getCreateTime);
        return lqw;
    }

    /**
     * 新增爱情清单
     */
    @Override
    public Boolean insertByBo(RepertoireBo bo) {
        Repertoire add = BeanUtil.toBean(bo, Repertoire.class);
        add.setCreateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改爱情清单
     */
    @Override
    public Boolean updateByBo(RepertoireBo bo) {
        Repertoire update = BeanUtil.toBean(bo, Repertoire.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除爱情清单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<RepertoireVo> repertoireApi(RepertoireBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Repertoire> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Repertoire::getName, bo.getName());
        lqw.orderByDesc(Repertoire::getCreateTime);
        Page<RepertoireVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
}
