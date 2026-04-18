package com.qixidi.business.service.impl.label;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.label.LabelInfoBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.user.UserFollow;
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
    public LabelInfoVo fdLabelInfo(Long id, Long type) {
        LabelInfoVo infoVo = baseMapper.selectVoById(id);
        String uuid = LoginHelper.getTripartiteUuid();
        if (ObjectUtils.isEmpty(uuid)) return infoVo;

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, uuid).eq(UserFollow::getType, type));
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
}

