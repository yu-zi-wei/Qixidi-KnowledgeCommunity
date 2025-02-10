package com.qixidi.business.service.impl.privateUser;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.entity.privateUser.PrivateNewsInfo;
import com.qixidi.business.domain.entity.privateUser.PrivateUser;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.mapper.privateUser.PrivateNewsInfoMapper;
import com.qixidi.business.mapper.privateUser.PrivateUserMapper;
import com.qixidi.business.service.privateUser.IPrivateUserService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 私信记录Service业务层处理
 *
 * @author aurora
 * @date 2023-03-23
 */
@RequiredArgsConstructor
@Service
public class PrivateUserServiceImpl implements IPrivateUserService {

    private final PrivateUserMapper baseMapper;
    private final PrivateNewsInfoMapper privateNewsInfoMapper;

    /**
     * 查询私信记录
     *
     * @param id 私信记录主键
     * @return 私信记录
     */
    @Override
    public PrivateUserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录
     */
    @Override
    public TableDataInfo<PrivateUserVo> queryPageList(PrivateUserBo bo, PageQuery pageQuery) {
        Page<PrivateUserVo> result = baseMapper.selectListXml(bo, pageQuery.build());
        if (CollectionUtils.isEmpty(result.getRecords())) return TableDataInfo.build(result);
        // 获取未读数据 条数
        List<PrivateUserVo> beenReadList = privateNewsInfoMapper.selectBeenReadList(bo.getUid(), result.getRecords());
        if (CollectionUtils.isEmpty(beenReadList)) return TableDataInfo.build(result);
        Map<String, Integer> collect = beenReadList.stream().collect(Collectors.toMap(PrivateUserVo::getTargetUid, PrivateUserVo::getUnreadCount));
        result.getRecords().forEach(item -> {
            if (collect.get(item.getTargetUid()) != null) {
                item.setUnreadCount(collect.get(item.getTargetUid()));
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录
     */
    @Override
    public List<PrivateUserVo> queryList(PrivateUserBo bo) {
        LambdaQueryWrapper<PrivateUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PrivateUser> buildQueryWrapper(PrivateUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PrivateUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), PrivateUser::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetUid()), PrivateUser::getTargetUid, bo.getTargetUid());
        return lqw;
    }

    /**
     * 新增私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    @Override
    public Boolean insertByBo(PrivateUserBo bo) {
        PrivateUser add = BeanUtil.toBean(bo, PrivateUser.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    @Override
    public Boolean updateByBo(PrivateUserBo bo) {
        PrivateUser update = BeanUtil.toBean(bo, PrivateUser.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除私信记录
     *
     * @param ids 需要删除的私信记录主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(String id) {
        String uuid = LoginHelper.getTripartiteUuid();
        privateNewsInfoMapper.delete(new QueryWrapper<PrivateNewsInfo>()
            .eq("uid", uuid).eq("reply_target_uid", id));
        return baseMapper.delete(new QueryWrapper<PrivateUser>()
            .eq("uid", uuid)
            .eq("target_uid", id)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAll() {
        String uuid = LoginHelper.getTripartiteUuid();
        privateNewsInfoMapper.delete(new QueryWrapper<PrivateNewsInfo>()
            .eq("uid", uuid));
        return baseMapper.delete(new QueryWrapper<PrivateUser>()
            .eq("uid", uuid)) > 0;
    }

    @Override
    public boolean addPrivateUser(String targetUid) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (StringUtils.isEmpty(uuid)) return true;
        PrivateUserVo privateUserVo = baseMapper.selectVoOne(new QueryWrapper<PrivateUser>()
            .eq("uid", uuid).eq("target_uid", targetUid));
        if (ObjectUtils.isNotEmpty(privateUserVo)) return true;
        PrivateUser privateUser = new PrivateUser().setUid(uuid)
            .setTargetUid(targetUid)
            .setCreateTime(new Date())
            .setUpdateTime(new Date());
        return baseMapper.insert(privateUser) > 0;
    }
}

