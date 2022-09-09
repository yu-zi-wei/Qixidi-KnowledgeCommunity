package com.aurora.business.service.impl.label;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.label.LabelInfoBo;
import com.aurora.business.domain.entity.label.LabelGroupingInfo;
import com.aurora.business.domain.entity.label.LabelInfo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.mapper.label.LabelGroupingInfoMapper;
import com.aurora.business.mapper.label.LabelInfoMapper;
import com.aurora.business.service.label.ILabelInfoService;
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
import java.util.stream.Collectors;

/**
 * 标签信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-16
 */
@RequiredArgsConstructor
@Service
public class LabelInfoServiceImpl implements ILabelInfoService {

    private final LabelInfoMapper baseMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;

    /**
     * 查询标签信息
     *
     * @param id 标签信息主键
     * @return 标签信息
     */
    @Override
    public LabelInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询标签信息列表
     *
     * @param bo 标签信息
     * @return 标签信息
     */
    @Override
    public TableDataInfo<LabelInfoVo> queryPageList(LabelInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LabelInfo> lqw = buildQueryWrapper(bo);
        Page<LabelInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<LabelGroupingInfo> labelGroupingInfos = labelGroupingInfoMapper.selectList();
        Map<Long, String> collect = labelGroupingInfos.stream().collect(Collectors.toMap(LabelGroupingInfo::getId, LabelGroupingInfo::getGroupingName));
        result.getRecords().forEach(item -> {
            if (collect.get(item.getLabelGroupingId()) != null) {
                item.setGroupingName(collect.get(item.getLabelGroupingId()));
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询标签信息列表
     *
     * @param bo 标签信息
     * @return 标签信息
     */
    @Override
    public List<LabelInfoVo> queryList(LabelInfoBo bo) {
        LambdaQueryWrapper<LabelInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LabelInfo> buildQueryWrapper(LabelInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LabelInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getLabelGroupingId() != null, LabelInfo::getLabelGroupingId, bo.getLabelGroupingId());
        lqw.like(StringUtils.isNotBlank(bo.getLabelName()), LabelInfo::getLabelName, bo.getLabelName());
        lqw.eq(StringUtils.isNotBlank(bo.getLabelDescribe()), LabelInfo::getLabelDescribe, bo.getLabelDescribe());
        lqw.eq(StringUtils.isNotBlank(bo.getLabelCover()), LabelInfo::getLabelCover, bo.getLabelCover());
        lqw.eq(bo.getState() != null, LabelInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增标签信息
     *
     * @param bo 标签信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(LabelInfoBo bo) {
        LabelInfo add = BeanUtil.toBean(bo, LabelInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改标签信息
     *
     * @param bo 标签信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(LabelInfoBo bo) {
        LabelInfo update = BeanUtil.toBean(bo, LabelInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(LabelInfo entity) {
        entity.setCreateTime(new Date());
        entity.setCreateBy(LoginHelper.getUserId());
    }

    /**
     * 批量删除标签信息
     *
     * @param ids 需要删除的标签信息主键
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

