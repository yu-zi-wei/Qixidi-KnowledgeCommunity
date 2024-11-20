package com.aurora.business.service.impl.topic;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.topic.TopicJoinInfoBo;
import com.aurora.business.domain.entity.topic.TopicJoinInfo;
import com.aurora.business.domain.vo.topic.TopicJoinInfoVo;
import com.aurora.business.mapper.topic.TopicJoinInfoMapper;
import com.aurora.business.service.topic.ITopicJoinInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 话题参与信息Service业务层处理
 *
 * @author aurora
 * @date 2023-03-09
 */
@RequiredArgsConstructor
@Service
public class TopicJoinInfoServiceImpl implements ITopicJoinInfoService {

    private final TopicJoinInfoMapper baseMapper;

    /**
     * 查询话题参与信息
     *
     * @param id 话题参与信息主键
     * @return 话题参与信息
     */
    @Override
    public TopicJoinInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询话题参与信息列表
     *
     * @param bo 话题参与信息
     * @return 话题参与信息
     */
    @Override
    public TableDataInfo<TopicJoinInfoVo> queryPageList(TopicJoinInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TopicJoinInfo> lqw = buildQueryWrapper(bo);
        Page<TopicJoinInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询话题参与信息列表
     *
     * @param bo 话题参与信息
     * @return 话题参与信息
     */
    @Override
    public List<TopicJoinInfoVo> queryList(TopicJoinInfoBo bo) {
        LambdaQueryWrapper<TopicJoinInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TopicJoinInfo> buildQueryWrapper(TopicJoinInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TopicJoinInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), TopicJoinInfo::getUid, bo.getUid());
        lqw.eq(bo.getTopicId() != null, TopicJoinInfo::getTopicId, bo.getTopicId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), TopicJoinInfo::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getEnclosure()), TopicJoinInfo::getEnclosure, bo.getEnclosure());
        lqw.eq(StringUtils.isNotBlank(bo.getEnclosureType()), TopicJoinInfo::getEnclosureType, bo.getEnclosureType());
        lqw.eq(bo.getState() != null, TopicJoinInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增话题参与信息
     *
     * @param bo 话题参与信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(TopicJoinInfoBo bo) {
        TopicJoinInfo add = BeanUtil.toBean(bo, TopicJoinInfo.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改话题参与信息
     *
     * @param bo 话题参与信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(TopicJoinInfoBo bo) {
        TopicJoinInfo update = BeanUtil.toBean(bo, TopicJoinInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除话题参与信息
     *
     * @param ids 需要删除的话题参与信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
