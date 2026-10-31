package com.qixidi.business.service.impl.label;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.label.LabelGroupingInfoBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.label.LabelGroupingInfo;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.service.label.ILabelGroupingInfoService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.exception.ServiceException;
import com.qixidi.auth.helper.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final LabelInfoMapper labelInfoMapper;
    private final ArticleInformationMapper articleInformationMapper;

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
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<LabelGroupingInfoVo> fdkGroupingList(LabelGroupingInfoBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(baseMapper.fdkGroupingList(bo, pageQuery.build()));
    }

    /**
     * 合并标签分组：源分组下的标签归属与文章分类迁移至目标分组后删除源分组（entriesNumber 由 LabelTask 定时重算）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeGrouping(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new ServiceException("源分组与目标分组不能相同");
        }
        if (baseMapper.selectById(sourceId) == null || baseMapper.selectById(targetId) == null) {
            throw new ServiceException("源分组或目标分组不存在");
        }
        // 分组下标签归属迁移
        labelInfoMapper.update(null, Wrappers.lambdaUpdate(LabelInfo.class)
            .set(LabelInfo::getLabelGroupingId, targetId)
            .eq(LabelInfo::getLabelGroupingId, sourceId));
        // 文章分类归属迁移（grouping_id 为单值外键）
        articleInformationMapper.update(null, Wrappers.lambdaUpdate(ArticleInformation.class)
            .set(ArticleInformation::getGroupingId, targetId)
            .eq(ArticleInformation::getGroupingId, sourceId));
        // 物理删除源分组（与 deleteWithValidByIds 行为一致，实体无 @TableLogic）
        baseMapper.deleteById(sourceId);
    }
}

