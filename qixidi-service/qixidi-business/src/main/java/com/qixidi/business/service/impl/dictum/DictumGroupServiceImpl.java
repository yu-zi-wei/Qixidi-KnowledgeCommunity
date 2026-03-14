package com.qixidi.business.service.impl.dictum;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.entity.dictum.DictumGroup;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.qixidi.business.mapper.dictum.DictumGroupMapper;
import com.qixidi.business.service.dictum.IDictumGroupService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 名言分组Service业务层处理
 *
 * @author aurora
 * @date 2023-04-24
 */
@RequiredArgsConstructor
@Service
public class DictumGroupServiceImpl implements IDictumGroupService {

    private final DictumGroupMapper baseMapper;

    /**
     * 查询名言分组
     *
     * @param id 名言分组主键
     * @return 名言分组
     */
    @Override
    public DictumGroupVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询名言分组列表
     *
     * @param bo 名言分组
     * @return 名言分组
     */
    @Override
    public TableDataInfo<DictumGroupVo> queryPageList(DictumGroupBo bo, PageQuery pageQuery) {
        IPage<DictumGroupVo> result = baseMapper.selectVoPageXml(bo, pageQuery.build());
        return TableDataInfo.build(result);
    }

    /**
     * 查询名言分组列表
     *
     * @param bo 名言分组
     * @return 名言分组
     */
    @Override
    public List<DictumGroupVo> queryList(DictumGroupBo bo) {
        LambdaQueryWrapper<DictumGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DictumGroup> buildQueryWrapper(DictumGroupBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DictumGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), DictumGroup::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCover()), DictumGroup::getCover, bo.getCover());
        lqw.eq(StringUtils.isNotBlank(bo.getBriefIntroduction()), DictumGroup::getBriefIntroduction, bo.getBriefIntroduction());
        lqw.eq(bo.getState() != null, DictumGroup::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增名言分组
     *
     * @param bo 名言分组
     * @return 结果
     */
    @Override
    public Boolean insertByBo(DictumGroupBo bo) {
        DictumGroup add = BeanUtil.toBean(bo, DictumGroup.class);
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改名言分组
     *
     * @param bo 名言分组
     * @return 结果
     */
    @Override
    public Boolean updateByBo(DictumGroupBo bo) {
        DictumGroup update = BeanUtil.toBean(bo, DictumGroup.class);
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除名言分组
     *
     * @param ids 需要删除的名言分组主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

