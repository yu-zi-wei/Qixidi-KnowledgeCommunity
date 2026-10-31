package com.qixidi.business.service.impl.label;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.EscapeUtil;
import com.light.exception.ServiceException;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.label.LabelInfoBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.enums.UserFollowTypeEnums;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.user.UserFollowVo;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.business.service.label.ILabelInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final UserFollowMapper userFollowMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;
    private final ArticleInformationMapper articleInformationMapper;

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
        bo.setLabelName(EscapeUtil.escapeChar(bo.getLabelName()));
        bo.setGroupingName(EscapeUtil.escapeChar(bo.getGroupingName()));
        Page<LabelInfoVo> result = baseMapper.selectVoPages(bo, pageQuery.build());
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
        lqw.in(CollUtil.isNotEmpty(bo.getLabelGroupingIds()), LabelInfo::getLabelGroupingId, bo.getLabelGroupingIds());
        lqw.in(CollUtil.isNotEmpty(bo.getStates()), LabelInfo::getState, bo.getStates());
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
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<LabelInfoVo> fdLabelList(LabelInfoBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        List<LabelInfoVo> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(bo.getLabelName())) return list;

        list = baseMapper.selectVoList(new LambdaQueryWrapper<LabelInfo>()
                .like(LabelInfo::getLabelName, bo.getLabelName()));
        if (ObjectUtils.isEmpty(bo.getUid()) || CollectionUtils.isEmpty(list)) return list;

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, bo.getUid())
                .eq(UserFollow::getType, bo.getType()));
        if (CollectionUtils.isEmpty(userFollowVos)) return list;

        Map<String, String> collect = userFollowVos.stream().collect(Collectors.toMap(UserFollowVo::getTargetId, UserFollowVo::getUid));
        list.forEach(item -> {
            if (collect.get(item.getId().toString()) != null) {
                item.setIsFollow(true);
            }
        });
        return list;
    }

    @Override
    public LabelInfoVo fdLabelInfo(Long id) {
        LabelInfoVo infoVo = baseMapper.selectVoById(id);
        String uuid = LoginHelper.getTripartiteUuid();
        if (ObjectUtils.isEmpty(uuid)) return infoVo;

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, uuid).eq(UserFollow::getType, UserFollowTypeEnums.LABEL_FOLLOW.getCode()));
        if (CollectionUtils.isEmpty(userFollowVos)) return infoVo;

        userFollowVos.forEach(item -> {
            if (Long.valueOf(item.getTargetId()).equals(infoVo.getId())) {
                infoVo.setIsFollow(true);
            }
        });
        return infoVo;
    }

    @Override
    public LabelGroupingInfoVo LabelGroupingInfo(Long id) {
        LabelGroupingInfoVo labelGroupingInfoVo = labelGroupingInfoMapper.selectVoById(id);
        if (ObjectUtils.isEmpty(labelGroupingInfoVo)) return labelGroupingInfoVo;
        Long articleNumber = articleInformationMapper.selectCount(new QueryWrapper<ArticleInformation>()
                .eq("grouping_id", id));
        labelGroupingInfoVo.setArticleNumber(articleNumber);
        return labelGroupingInfoVo;
    }

    @Override
    public List<LabelInfoVo> systemLabel(String label) {
        List<LabelInfoVo> list = baseMapper.selectVoList(new LambdaQueryWrapper<LabelInfo>()
                .like(StringUtils.isNotBlank(label), LabelInfo::getLabelName, label)
                .orderByDesc(LabelInfo::getArticleNumber, LabelInfo::getFollowNumber, LabelInfo::getCreateTime));
        String uuid = LoginHelper.getTripartiteUuid();
        if (StringUtils.isEmpty(uuid)) return list;
        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getType, 2));
        if (CollectionUtils.isEmpty(userFollowVos)) return list;

        Map<String, String> collect = new HashMap<>();
        userFollowVos.forEach(item -> {
            if (item.getUid().equals(uuid)) {
                collect.put(item.getTargetId(), item.getUid());
            }
        });
        Map<String, Long> groupCounts = userFollowVos.stream()
                .collect(Collectors.groupingBy(UserFollowVo::getTargetId, Collectors.counting()));
        list.forEach(item -> {
            Long sum = groupCounts.get(item.getId().toString());
            if (sum != null) {
                item.setFollowNumber(Integer.valueOf(sum.toString()));
            }
            if (collect.get(item.getId().toString()) != null) {
                item.setIsFollow(true);
            }
        });
        return list;
    }

    /**
     * 合并标签：源标签的文章挂载与关注迁移至目标标签后删除源标签（articleNumber/followNumber 由 LabelTask 定时重算）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeLabel(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new ServiceException("源标签与目标标签不能相同");
        }
        if (baseMapper.selectById(sourceId) == null || baseMapper.selectById(targetId) == null) {
            throw new ServiceException("源标签或目标标签不存在");
        }
        // 文章迁移：label_id 为逗号分隔 id 串，FIND_IN_SET 精确匹配防误伤（1 不匹配 12）
        // 已挂目标标签的文章仅移除源 id；未挂的将源 id 替换为目标 id，替换天然去重
        articleInformationMapper.update(null, Wrappers.lambdaUpdate(ArticleInformation.class)
            .setSql("label_id = TRIM(BOTH ',' FROM REPLACE(CONCAT(',', label_id, ','), CONCAT(',', {0}, ','), ','))", sourceId)
            .apply("FIND_IN_SET({0}, label_id) > 0 AND FIND_IN_SET({1}, label_id) > 0", sourceId, targetId));
        articleInformationMapper.update(null, Wrappers.lambdaUpdate(ArticleInformation.class)
            .setSql("label_id = TRIM(BOTH ',' FROM REPLACE(CONCAT(',', label_id, ','), CONCAT(',', {0}, ','), CONCAT(',', {1}, ',')))", sourceId, targetId)
            .apply("FIND_IN_SET({0}, label_id) > 0 AND FIND_IN_SET({1}, label_id) = 0", sourceId, targetId));
        // 关注迁移：已关注目标标签的用户删其对源标签的关注（防 uid+目标重复），其余改挂目标标签
        List<UserFollow> srcFollows = userFollowMapper.selectList(Wrappers.lambdaQuery(UserFollow.class)
            .eq(UserFollow::getTargetId, String.valueOf(sourceId))
            .eq(UserFollow::getType, UserFollowTypeEnums.LABEL_FOLLOW.getCode()));
        if (!srcFollows.isEmpty()) {
            List<Long> bothIds = userFollowMapper.selectList(Wrappers.lambdaQuery(UserFollow.class)
                .in(UserFollow::getUid, srcFollows.stream().map(UserFollow::getUid).toList())
                .eq(UserFollow::getTargetId, String.valueOf(targetId))
                .eq(UserFollow::getType, UserFollowTypeEnums.LABEL_FOLLOW.getCode()))
                .stream().map(UserFollow::getId).toList();
            if (!bothIds.isEmpty()) {
                userFollowMapper.deleteByIds(bothIds);
            }
            userFollowMapper.update(null, Wrappers.lambdaUpdate(UserFollow.class)
                .set(UserFollow::getTargetId, String.valueOf(targetId))
                .eq(UserFollow::getTargetId, String.valueOf(sourceId))
                .eq(UserFollow::getType, UserFollowTypeEnums.LABEL_FOLLOW.getCode()));
        }
        // 物理删除源标签（与 deleteWithValidByIds 行为一致，实体无 @TableLogic）
        baseMapper.deleteById(sourceId);
    }
}

