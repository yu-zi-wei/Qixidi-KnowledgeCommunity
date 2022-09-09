package com.aurora.business.service.impl.label;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.label.LabelGroupingInfoBo;
import com.aurora.business.domain.entity.label.LabelGroupingInfo;
import com.aurora.business.domain.vo.label.LabelGroupingInfoVo;
import com.aurora.business.mapper.label.LabelGroupingInfoMapper;
import com.aurora.business.service.label.ILabelGroupingInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
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
 * 标签分组信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-16
 */
@RequiredArgsConstructor
@Service
public class LabelGroupingInfoServiceImpl implements ILabelGroupingInfoService {

    private final LabelGroupingInfoMapper baseMapper;

    /**
     * 查询标签分组信息
     *
     * @param id 标签分组信息主键
     * @return 标签分组信息
     */
    @Override
    public LabelGroupingInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询标签分组信息列表
     *
     * @param bo 标签分组信息
     * @return 标签分组信息
     */
    @Override
    public TableDataInfo<LabelGroupingInfoVo> queryPageList(LabelGroupingInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LabelGroupingInfo> lqw = buildQueryWrapper(bo);
        Page<LabelGroupingInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询标签分组信息列表
     *
     * @param bo 标签分组信息
     * @return 标签分组信息
     */
    @Override
    public List<LabelGroupingInfoVo> queryList(LabelGroupingInfoBo bo) {
        LambdaQueryWrapper<LabelGroupingInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LabelGroupingInfo> buildQueryWrapper(LabelGroupingInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LabelGroupingInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getGroupingName()), LabelGroupingInfo::getGroupingName, bo.getGroupingName());
        lqw.eq(bo.getState() != null, LabelGroupingInfo::getState, bo.getState());
        lqw.eq(StringUtils.isNotBlank(bo.getGroupingDescribe()), LabelGroupingInfo::getGroupingDescribe, bo.getGroupingDescribe());
        return lqw;
    }

    /**
     * 新增标签分组信息
     *
     * @param bo 标签分组信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(LabelGroupingInfoBo bo) {
        LabelGroupingInfo add = BeanUtil.toBean(bo, LabelGroupingInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改标签分组信息
     *
     * @param bo 标签分组信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(LabelGroupingInfoBo bo) {
        LabelGroupingInfo update = BeanUtil.toBean(bo, LabelGroupingInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(LabelGroupingInfo entity) {
        entity.setCreateTime(new Date());
        entity.setCreateBy(LoginHelper.getUserId());
    }

    /**
     * 批量删除标签分组信息
     *
     * @param ids 需要删除的标签分组信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

