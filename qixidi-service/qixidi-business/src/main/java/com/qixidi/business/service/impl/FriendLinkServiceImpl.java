package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.page.TableDataInfo;
import com.light.exception.ServiceException;
import com.qixidi.business.domain.bo.FriendLinkBo;
import com.qixidi.business.domain.entity.FriendLink;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.qixidi.business.mapper.FriendLinkMapper;
import com.qixidi.business.service.IFriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 友链管理（b_friend_link 无逻辑删除列，删除即物理删）
 *
 * @author ziwei
 */
@RequiredArgsConstructor
@Service
public class FriendLinkServiceImpl implements IFriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    @Override
    public TableDataInfo<FriendLinkVo> queryPageList(FriendLinkBo bo) {
        LambdaQueryWrapper<FriendLink> wrapper = new LambdaQueryWrapper<FriendLink>()
                .like(StrUtil.isNotBlank(bo.getLinkName()), FriendLink::getLinkName, bo.getLinkName())
                .in(CollectionUtil.isNotEmpty(bo.getStatuses()), FriendLink::getStatus, bo.getStatuses())
                .orderByDesc(FriendLink::getCreateTime)
                .orderByDesc(FriendLink::getId);
        Page<FriendLink> page = friendLinkMapper.selectPage(bo.build(), wrapper);
        TableDataInfo<FriendLinkVo> table = new TableDataInfo<>();
        table.setTotal(page.getTotal());
        table.setRows(BeanUtil.copyToList(page.getRecords(), FriendLinkVo.class));
        return table;
    }

    @Override
    public FriendLinkVo queryById(Long id) {
        FriendLink friendLink = friendLinkMapper.selectById(id);
        return BeanUtil.copyProperties(friendLink, FriendLinkVo.class);
    }

    @Override
    public void insertByBo(FriendLinkBo bo) {
        FriendLink friendLink = BeanUtil.copyProperties(bo, FriendLink.class);
        friendLink.setId(null);
        friendLink.setCreateTime(new Date());
        friendLinkMapper.insert(friendLink);
    }

    @Override
    public void updateByBo(FriendLinkBo bo) {
        if (bo.getId() == null || friendLinkMapper.selectById(bo.getId()) == null) {
            throw new ServiceException("友链不存在");
        }
        FriendLink friendLink = BeanUtil.copyProperties(bo, FriendLink.class);
        friendLinkMapper.updateById(friendLink);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        friendLinkMapper.deleteByIds(ids);
    }
}
