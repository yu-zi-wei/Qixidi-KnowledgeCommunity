package com.aurora.business.service.impl.topic;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.topic.TopicInfoBo;
import com.aurora.business.domain.entity.topic.TopicInfo;
import com.aurora.business.domain.vo.topic.TopicInfoVo;
import com.aurora.business.mapper.topic.TopicInfoMapper;
import com.aurora.business.service.topic.ITopicInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 话题信息Service业务层处理
 *
 * @author aurora
 * @date 2023-03-09
 */
@RequiredArgsConstructor
@Service
public class TopicInfoServiceImpl implements ITopicInfoService {

    private final TopicInfoMapper baseMapper;

    /**
     * 查询话题信息
     *
     * @param id 话题信息主键
     * @return 话题信息
     */
    @Override
    public TopicInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询话题信息列表
     *
     * @param bo 话题信息
     * @return 话题信息
     */
    @Override
    public TableDataInfo<TopicInfoVo> queryPageList(TopicInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TopicInfo> lqw = buildQueryWrapper(bo);
        Page<TopicInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询话题信息列表
     *
     * @param bo 话题信息
     * @return 话题信息
     */
    @Override
    public List<TopicInfoVo> queryList(TopicInfoBo bo) {
        LambdaQueryWrapper<TopicInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TopicInfo> buildQueryWrapper(TopicInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TopicInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCover()), TopicInfo::getCover, bo.getCover());
        lqw.like(StringUtils.isNotBlank(bo.getTopicName()), TopicInfo::getTopicName, bo.getTopicName());
        lqw.eq(StringUtils.isNotBlank(bo.getDescribe()), TopicInfo::getDescribe, bo.getDescribe());
        lqw.eq(bo.getOrder() != null, TopicInfo::getOrder, bo.getOrder());
        lqw.eq(bo.getState() != null, TopicInfo::getState, bo.getState());
        lqw.eq(bo.getNumber() != null, TopicInfo::getNumber, bo.getNumber());
        lqw.eq(bo.getStartTime() != null, TopicInfo::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, TopicInfo::getEndTime, bo.getEndTime());
        return lqw;
    }

    /**
     * 新增话题信息
     *
     * @param bo 话题信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(TopicInfoBo bo) {
        TopicInfo add = BeanUtil.toBean(bo, TopicInfo.class);
        add.setCreateTime(new Date());
        add.setCreateBy(LoginHelper.getUsername());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改话题信息
     *
     * @param bo 话题信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(TopicInfoBo bo) {
        TopicInfo update = BeanUtil.toBean(bo, TopicInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除话题信息
     *
     * @param ids 需要删除的话题信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
