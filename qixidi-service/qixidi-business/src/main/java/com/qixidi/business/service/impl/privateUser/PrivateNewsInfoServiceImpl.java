package com.qixidi.business.service.impl.privateUser;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.DateUtils;
import com.light.core.utils.StringUtils;

import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.entity.privateUser.PrivateNewsInfo;
import com.qixidi.business.domain.entity.privateUser.PrivateUser;
import com.qixidi.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.mapper.privateUser.PrivateNewsInfoMapper;
import com.qixidi.business.mapper.privateUser.PrivateUserMapper;
import com.qixidi.business.service.ITripartiteUserService;
import com.qixidi.business.service.privateUser.IPrivateNewsInfoService;
import com.qixidi.business.service.privateUser.IPrivateUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 私信记录Service业务层处理
 *
 * @author aurora
 * @date 2023-03-23
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class PrivateNewsInfoServiceImpl implements IPrivateNewsInfoService {

    private final PrivateNewsInfoMapper baseMapper;
    private final PrivateUserMapper privateUserMapper;
    private final IPrivateUserService iPrivateUserService;
    private final ITripartiteUserService tripartiteUserService;

    /**
     * 查询私信记录
     *
     * @param id 私信记录主键
     * @return 私信记录
     */
    @Override
    public PrivateNewsInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录
     */
    @Override
    public TableDataInfo<PrivateNewsInfoVo> queryPageList(PrivateNewsInfoBo bo, PageQuery pageQuery) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        Page<PrivateNewsInfoVo> result = baseMapper.selectListXml(bo, pageQuery.build());
        fillUserDisplay(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 后台全站私信消息分页：走 selectAdminList（双方昵称/头像由 Service 组装），不覆盖入参 uid
     */
    @Override
    public TableDataInfo<PrivateNewsInfoVo> adminQueryPageList(PrivateNewsInfoBo bo, PageQuery pageQuery) {
        // 发送/接收方昵称搜索：用户域查 uuid 集合后 in（空命中短路返回空页）
        List<String> senderUids = null;
        if (StringUtils.isNotBlank(bo.getSenderNickname())) {
            senderUids = tripartiteUserService.getUidsByNickname(bo.getSenderNickname());
            if (senderUids.isEmpty()) {
                return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
            }
        }
        List<String> recipientUids = null;
        if (StringUtils.isNotBlank(bo.getRecipientNickname())) {
            recipientUids = tripartiteUserService.getUidsByNickname(bo.getRecipientNickname());
            if (recipientUids.isEmpty()) {
                return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
            }
        }
        Page<PrivateNewsInfoVo> result = baseMapper.selectAdminList(bo, senderUids, recipientUids, pageQuery.build());
        fillUserDisplay(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 组装消息双方展示字段：发送方（键 uid）填昵称/头像，接收方（键 replyTargetUid）填对方昵称/头像
     */
    private void fillUserDisplay(List<PrivateNewsInfoVo> records) {
        if (records == null || records.isEmpty()) return;
        Set<String> uids = new HashSet<>();
        records.forEach(r -> {
            if (StringUtils.isNotBlank(r.getUid())) uids.add(r.getUid());
            if (StringUtils.isNotBlank(r.getReplyTargetUid())) uids.add(r.getReplyTargetUid());
        });
        if (uids.isEmpty()) return;
        Map<String, TripartiteUserVo> users = tripartiteUserService.getUserByUids(uids).stream()
                .collect(Collectors.toMap(TripartiteUserVo::getUuid, u -> u, (a, b) -> a));
        records.forEach(r -> {
            TripartiteUserVo sender = users.get(r.getUid());
            if (sender != null) {
                r.setNickname(sender.getNickname());
                r.setUserAvatar(sender.getAvatar());
            }
            TripartiteUserVo target = users.get(r.getReplyTargetUid());
            if (target != null) {
                r.setTargetName(target.getNickname());
                r.setTargetAvatar(target.getAvatar());
            }
        });
    }

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录
     */
    @Override
    public List<PrivateNewsInfoVo> queryList(PrivateNewsInfoBo bo) {
        LambdaQueryWrapper<PrivateNewsInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PrivateNewsInfo> buildQueryWrapper(PrivateNewsInfoBo bo) {
        LambdaQueryWrapper<PrivateNewsInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), PrivateNewsInfo::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getNewsComment()), PrivateNewsInfo::getNewsComment, bo.getNewsComment());
        lqw.eq(StringUtils.isNotBlank(bo.getReplyTargetUid()), PrivateNewsInfo::getReplyTargetUid, bo.getReplyTargetUid());
        return lqw;
    }

    /**
     * 发送私信
     *
     * @param bo 私信
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(PrivateNewsInfoBo bo) {
        PrivateNewsInfo add = BeanUtil.toBean(bo, PrivateNewsInfo.class);
        String uuid = LoginHelper.getTripartiteUuid();
        add.setUid(uuid);
        add.setCreateTime(new Date());
        String replyTargetUid = bo.getReplyTargetUid();
        String webSocketUuid = replyTargetUid + ":" + uuid;
        Boolean online = WebSocketUtils.containsKey(webSocketUuid);
        if (online) {
            add.setBeenRead(2);// 当前私信在线(默认已读)
        }
        // 对比消息 更新时间标识
        PrivateNewsInfoVo privateNewsInfoVo = baseMapper.selectLast(uuid, replyTargetUid);
        if (ObjectUtils.isNotEmpty(privateNewsInfoVo)) {
            Map<String, Integer> datePoor = DateUtils.getDatePoor(privateNewsInfoVo.getCreateTime(), new Date());
            if (datePoor.get("min") > 20) {
                baseMapper.update(new LambdaUpdateWrapper<PrivateNewsInfo>()
                        .set(PrivateNewsInfo::getTimeMark, 1)
                        .eq(PrivateNewsInfo::getId, privateNewsInfoVo.getId()));
            }
        }
//        添加消息
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        updatePrivateNews(uuid, bo.getReplyTargetUid(), bo.getNewsComment());
        if (online) {
            //        webSocket私信推送
            PageQuery pageQuery = new PageQuery();
            bo.setUid(uuid);
            pageQuery.setPageNum(0);
            pageQuery.setPageSize(1024);
            Page<PrivateNewsInfoVo> result = baseMapper.selectListXml(bo, pageQuery.build());
            fillUserDisplay(result.getRecords());
            PrivateUserBo privateUserBo = new PrivateUserBo();
            privateUserBo.setUid(add.getReplyTargetUid());

            TableDataInfo<PrivateUserVo> privateUserVoTableDataInfo = iPrivateUserService.queryPageList(privateUserBo, pageQuery);
            Map<String, Object> map = new HashMap<>();
            map.put("newsList", result.getRecords());
            map.put("userList", privateUserVoTableDataInfo.getRows());

            //webSocket私信推送
            WebSocketUtils.sendMessage(webSocketUuid, map);
            return flag;
        }
        //        webSocket推送系统消息
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(replyTargetUid);
        //        webSocket推送私信红点
        WebSocketSelector.execute(WebSocketEnum.PERSONAL_RED_DOT).execute(replyTargetUid);
        log.info("webSocket消息推送:{}", replyTargetUid);
        return flag;
    }

    public void updatePrivateNews(String uid, String replyTargetUid, String newsComment) {

        PrivateUserVo privateUserVo = privateUserMapper.selectVoOne(new LambdaQueryWrapper<PrivateUser>()
                .eq(PrivateUser::getTargetUid, uid).eq(PrivateUser::getUid, replyTargetUid));
        if (ObjectUtils.isEmpty(privateUserVo)) {
            PrivateUser privateUser = new PrivateUser().setUid(replyTargetUid)
                    .setTargetUid(uid)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            privateUserMapper.insert(privateUser);
        }
        // 更新用户私信表
        privateUserMapper.update(new LambdaUpdateWrapper<PrivateUser>()
                .set(PrivateUser::getLastNews, newsComment)
                .set(PrivateUser::getUpdateTime, new Date())
                .eq(PrivateUser::getUid, uid)
                .eq(PrivateUser::getTargetUid, replyTargetUid)
                .or(wrapper -> {
                    wrapper.eq(PrivateUser::getTargetUid, uid)
                            .eq(PrivateUser::getUid, replyTargetUid);
                }));
    }

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    @Override
    public Boolean updateByBo(PrivateNewsInfoBo bo) {
        PrivateNewsInfo update = BeanUtil.toBean(bo, PrivateNewsInfo.class);
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
    public void beenRead(String targetUid) {
        String uuid = LoginHelper.getTripartiteUuid();
        baseMapper.update(new LambdaUpdateWrapper<PrivateNewsInfo>()
                .set(PrivateNewsInfo::getBeenRead, 2)
                .eq(PrivateNewsInfo::getReplyTargetUid, uuid)
                .eq(PrivateNewsInfo::getUid, targetUid));
        //        webSocket站内消息推送
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uuid);
        //        webSocket站内私信红点消息推送
        WebSocketSelector.execute(WebSocketEnum.PERSONAL_RED_DOT).execute(uuid);

    }
}

