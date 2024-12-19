package com.aurora.business.service.impl.dictum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurora.business.domain.bo.dictum.DictumCommentBo;
import com.aurora.business.domain.entity.dictum.DictumComment;
import com.aurora.business.domain.vo.dictum.DictumCommentVo;
import com.aurora.business.mapper.TripartiteUserMapper;
import com.aurora.business.mapper.dictum.DictumCommentMapper;
import com.aurora.business.service.dictum.DictumCommentService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.StatusEnums;
import com.aurora.common.exception.ServiceException;
import com.aurora.common.helper.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zi-wei
 * @create 2024/12/17 14:15
 */
@Service
public class DictumCommentServiceImpl implements DictumCommentService {
    @Autowired
    private DictumCommentMapper dictumCommentMapper;
    @Autowired
    private TripartiteUserMapper tripartiteUserMapper;

    @Override
    public void add(DictumCommentBo bo) {
        DictumComment dictumComment = BeanUtil.copyProperties(bo, DictumComment.class);
        dictumComment.setCommentUid(LoginHelper.getTripartiteUuid());
        dictumComment.setCreateTime(new Date());
        if (bo.getType() == 2) {//二级评论
            Long l = dictumCommentMapper.selectCount(new LambdaQueryWrapper<DictumComment>()
                .eq(DictumComment::getParentId, bo.getParentId())
                .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode()));
            if (l >= 1000) {//子集评论最大挂载1000条数据
                throw new ServiceException("子级评论已达上限，请新开评论进行回复！");
            }
        }
        int insert = dictumCommentMapper.insert(dictumComment);
        if (insert < 0) {
            throw new ServiceException("添加失败");
        }
    }

    @Override
    public void delete(Long id) {
        dictumCommentMapper.update(null, new LambdaUpdateWrapper<DictumComment>()
            .eq(DictumComment::getId, id)
            .set(DictumComment::getStatus, StatusEnums.DELETE.getCode()));
    }

    @Override
    public TableDataInfo<DictumCommentVo> commentList(Long id, PageQuery pageQuery) {
        //一级评论
        IPage<DictumCommentVo> dictumCommentVoIPage = dictumCommentMapper.selectVoPage(pageQuery.build(),
            new LambdaQueryWrapper<DictumComment>().eq(DictumComment::getDictumId, id)
                .eq(DictumComment::getType, 1)
                .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode())
                .orderByDesc(DictumComment::getCreateTime));
        List<DictumCommentVo> records = dictumCommentVoIPage.getRecords();
        if (CollectionUtil.isEmpty(records)) return new TableDataInfo();

        //二级评论
        List<Long> ids = records.stream().map(DictumCommentVo::getId).collect(Collectors.toList());
        List<DictumCommentVo> dictumCommentLevelIPage = dictumCommentMapper.selectVoList(new LambdaQueryWrapper<DictumComment>()
            .in(DictumComment::getParentId, ids)
            .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode())
            .orderByAsc(DictumComment::getCreateTime));

        Set<String> uids = new HashSet<>();
        records.forEach(item -> uids.add(item.getCommentUid()));
        if (CollectionUtil.isNotEmpty(dictumCommentLevelIPage)) {
            dictumCommentLevelIPage.forEach(item -> uids.add(item.getTargetUid()));
        }

        List<TripartiteUser> tripartiteUserVos = tripartiteUserMapper.selectList(new LambdaQueryWrapper<TripartiteUser>()
            .in(TripartiteUser::getUuid, uids));
        Map<String, TripartiteUser> userMpa = tripartiteUserVos.stream().collect(Collectors.toMap(TripartiteUser::getUuid, item -> item));
        Map<Long, List<DictumCommentVo>> levelmap = new HashMap<>();
        for (DictumCommentVo dictumCommentVo : dictumCommentLevelIPage) {
            List<DictumCommentVo> dictumCommentVos = levelmap.get(dictumCommentVo.getParentId());
            if (dictumCommentVos == null) dictumCommentVos = new ArrayList<>();
            //填充用户信息
            TripartiteUser user = userMpa.get(dictumCommentVo.getCommentUid());
            dictumCommentVo.setUsername(user.getUsername());
            dictumCommentVo.setNickname(user.getNickname());
            dictumCommentVo.setAvatar(user.getAvatar());
            TripartiteUser targetUser = userMpa.get(dictumCommentVo.getTargetUid());
            dictumCommentVo.setTargetUsername(targetUser.getUsername());
            dictumCommentVo.setTargetNickname(targetUser.getNickname());
            dictumCommentVo.setTargetAvatar(targetUser.getAvatar());
            dictumCommentVos.add(dictumCommentVo);
            levelmap.put(dictumCommentVo.getParentId(), dictumCommentVos);
        }
        //组装数据
        for (DictumCommentVo record : records) {
            TripartiteUser user = userMpa.get(record.getCommentUid());
            record.setUsername(user.getUsername());
            record.setNickname(user.getNickname());
            record.setAvatar(user.getAvatar());
            List<DictumCommentVo> dictumCommentVos = levelmap.get(record.getId());
            if (dictumCommentVos != null) {
                record.setDictumCommentVoList(dictumCommentVos);
            }
        }
        return TableDataInfo.build(records);
    }
}
