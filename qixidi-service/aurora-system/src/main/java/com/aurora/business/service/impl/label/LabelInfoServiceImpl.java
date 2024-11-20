package com.aurora.business.service.impl.label;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.label.LabelInfoBo;
import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.entity.label.LabelInfo;
import com.aurora.business.domain.entity.user.UserFollow;
import com.aurora.business.domain.vo.label.LabelGroupingInfoVo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.domain.vo.user.UserFollowVo;
import com.aurora.business.mapper.article.ArticleInformationMapper;
import com.aurora.business.mapper.label.LabelGroupingInfoMapper;
import com.aurora.business.mapper.label.LabelInfoMapper;
import com.aurora.business.mapper.user.UserFollowMapper;
import com.aurora.business.service.label.ILabelInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

        list = baseMapper.selectVoList(new QueryWrapper<LabelInfo>()
            .like("label_name", bo.getLabelName()));
        if (ObjectUtils.isEmpty(bo.getUid()) || CollectionUtils.isEmpty(list)) return list;

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new QueryWrapper<UserFollow>()
            .eq("uid", bo.getUid()).eq("type", bo.getType()));
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

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new QueryWrapper<UserFollow>()
            .eq("uid", uuid).eq("type", type));
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
        List<LabelInfoVo> list = new ArrayList<>();
        if (StringUtils.isNotBlank(label)) {
            list = baseMapper.selectVoList(new QueryWrapper<LabelInfo>().like("label_name", label));
        } else {
            list = baseMapper.selectVoList(null);
        }
        String uuid = LoginHelper.getTripartiteUuid();
        if (StringUtils.isEmpty(uuid)) return list;
        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new QueryWrapper<UserFollow>().eq("type", 2));
        if (CollectionUtils.isEmpty(userFollowVos)) return list;

        Map<String, String> collect = userFollowVos.stream().filter(item -> item.getUid().equals(uuid))
            .collect(Collectors.toMap(UserFollowVo::getTargetId, UserFollowVo::getUid));
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

