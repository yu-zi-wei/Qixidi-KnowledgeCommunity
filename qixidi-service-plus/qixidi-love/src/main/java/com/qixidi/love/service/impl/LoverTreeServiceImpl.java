/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 17:00
 * @version 1.0
 */
package com.qixidi.love.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.love.domain.LoverTree;
import com.qixidi.love.domain.bo.LoverTreeBo;
import com.qixidi.love.domain.vo.LoverTreeVo;
import com.qixidi.love.mapper.LoverTreeMapper;
import com.qixidi.love.service.ILoverTreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 爱情树Service业务层处理
 *
 * @author ziwei
 * @date 2022-11-30
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class LoverTreeServiceImpl implements ILoverTreeService {

    private final LoverTreeMapper baseMapper;

    /**
     * 查询爱情树
     */
    @Override
    public LoverTreeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询爱情树列表
     */
    @Override
    public TableDataInfo<LoverTreeVo> queryPageList(LoverTreeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverTree> lqw = buildQueryWrapper(bo);
        lqw.select(LoverTree.class, f -> !f.getColumn().equals("content"));
        Page<LoverTreeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询爱情树列表
     */
    @Override
    public List<LoverTreeVo> queryList(LoverTreeBo bo) {
        LambdaQueryWrapper<LoverTree> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LoverTree> buildQueryWrapper(LoverTreeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LoverTree> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), LoverTree::getContent, bo.getContent());
        lqw.eq(bo.getState() != null, LoverTree::getState, bo.getState());
        lqw.eq(bo.getType() != null, LoverTree::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增爱情树
     */
    @Override
    public Boolean insertByBo(LoverTreeBo bo) {
        LoverTree add = BeanUtil.toBean(bo, LoverTree.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改爱情树
     */
    @Override
    public Boolean updateByBo(LoverTreeBo bo) {
        LoverTree update = BeanUtil.toBean(bo, LoverTree.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除爱情树
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public LoverTreeVo loverTreeApi() {
        return baseMapper.loverTreeApi();
    }
}
