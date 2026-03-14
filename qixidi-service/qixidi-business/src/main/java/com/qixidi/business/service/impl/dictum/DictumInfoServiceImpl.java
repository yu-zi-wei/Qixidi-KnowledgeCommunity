package com.qixidi.business.service.impl.dictum;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.MsgEnums;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.entity.dictum.DictumComment;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.dictum.DictumCommentMapper;
import com.qixidi.business.mapper.dictum.DictumGroupMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.service.dictum.IDictumInfoService;
import com.qixidi.common.domain.enums.StatusEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 名言信息Service业务层处理
 *
 * @author aurora
 * @date 2023-04-24
 */
@RequiredArgsConstructor
@Service
public class DictumInfoServiceImpl implements IDictumInfoService {

    private final DictumInfoMapper baseMapper;
    private final DictumGroupMapper dictumGroupMapper;
    private final TripartiteUserMapper tripartiteUserMapper;
    @Autowired
    private DictumCommentMapper dictumCommentMapper;

    /**
     * 查询名言信息
     *
     * @param id 名言信息主键
     * @return 名言信息
     */
    @Override
    public DictumInfoVo queryById(Long id) {
        DictumInfoVo dictumInfoVo = baseMapper.selectVoById(id);
        TripartiteUserVo tripartiteUserVo = tripartiteUserMapper.selectWebInfo(dictumInfoVo.getUid());
        dictumInfoVo.setTripartiteUser(tripartiteUserVo);
        return dictumInfoVo;
    }

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息
     */
    @Override
    public TableDataInfo<DictumInfoVo> queryPageList(DictumInfoBo bo, PageQuery pageQuery) {
        IPage<DictumInfoVo> result = baseMapper.selectVoPageXml(bo, pageQuery.build());
        List<DictumInfoVo> records = result.getRecords();
        if (ObjectUtils.isEmpty(records)) return TableDataInfo.build();
        List<Long> ids = new ArrayList<>();
        Set<String> uids = new HashSet<>();
        records.forEach(item -> {
            ids.add(item.getId());
            uids.add(item.getUid());
        });
        List<DictumComment> dictumComments = dictumCommentMapper.selectList(new LambdaQueryWrapper<DictumComment>()
                .select(DictumComment::getId, DictumComment::getDictumId)
                .in(DictumComment::getDictumId, ids)
                .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode())
        );
        Map<Long, Long> sumMap = dictumComments.stream().collect(Collectors.groupingBy(DictumComment::getDictumId, Collectors.counting()));

        records.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getLabel())) {
                List<String> labelList = Arrays.asList(item.getLabel().split(","));
                item.setLabelList(labelList);
            }
            if (StringUtils.isNotEmpty(item.getPicture())) {
                List<String> pictureList = Arrays.asList(item.getPicture().split(","));
                item.setPictureList(pictureList);
            }
            Long sum = sumMap.get(item.getId());
            if (sum != null) {
                item.setCommentSum(sum);
            }
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息
     */
    @Override
    public List<DictumInfoVo> queryList(DictumInfoBo bo) {
        LambdaQueryWrapper<DictumInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DictumInfo> buildQueryWrapper(DictumInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DictumInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), DictumInfo::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), DictumInfo::getContent, bo.getContent());
        lqw.eq(bo.getGroupId() != null, DictumInfo::getGroupId, bo.getGroupId());
        lqw.eq(StringUtils.isNotBlank(bo.getLabel()), DictumInfo::getLabel, bo.getLabel());
        lqw.eq(StringUtils.isNotBlank(bo.getPicture()), DictumInfo::getPicture, bo.getPicture());
        lqw.eq(bo.getDictumState() != null, DictumInfo::getDictumState, bo.getDictumState());
        lqw.eq(bo.getState() != null, DictumInfo::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean insertByBo(DictumInfoBo bo) {
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        DictumInfo add = BeanUtil.toBean(bo, DictumInfo.class);
        add.setUid(LoginHelper.getTripartiteUuid());
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        dictumGroupMapper.addEmploySum(bo.getGroupId());
        if (ObjectUtils.isNotEmpty(bo.getAlbumId())) {
            baseMapper.addEmploy(bo.getAlbumId());
        }
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(DictumInfoBo bo) {
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        DictumInfo update = BeanUtil.toBean(bo, DictumInfo.class);
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除名言信息
     *
     * @param ids 需要删除的名言信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithValidById(Long id, Long groupId) {
        dictumGroupMapper.deleteEmploySum(groupId);
        DictumInfoVo dictumInfoVo = baseMapper.selectVoOne(new LambdaQueryWrapper<DictumInfo>()
                .eq(DictumInfo::getId, id));
        if (ObjectUtils.isNotEmpty(dictumInfoVo.getAlbumId())) {
            baseMapper.deleteEmploy(dictumInfoVo.getAlbumId());
        }
        return baseMapper.deleteById(id) > 0;
    }
}

