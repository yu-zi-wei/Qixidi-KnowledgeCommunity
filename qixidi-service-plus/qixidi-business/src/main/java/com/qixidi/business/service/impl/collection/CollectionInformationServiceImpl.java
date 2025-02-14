package com.qixidi.business.service.impl.collection;


import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.collection.CollectionInformationBo;
import com.qixidi.business.domain.bo.collection.CollectionRecordBo;
import com.qixidi.business.domain.entity.collection.CollectionInformation;
import com.qixidi.business.domain.entity.collection.CollectionRecord;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.collection.CollectionInformationVo;
import com.qixidi.business.mapper.collection.CollectionInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.service.collection.ICollectionInformationService;
import com.qixidi.business.service.impl.article.ArticleInformationServiceImpl;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.enums.CountUserType;
import com.qixidi.auth.helper.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
 * 收藏夹信息Service业务层处理 information
 *
 * @author aurora
 * @date 2022-09-29
 */
@RequiredArgsConstructor
@Service
public class CollectionInformationServiceImpl implements ICollectionInformationService {

    private final CollectionInformationMapper baseMapper;
    private final CollectionRecordMapper collectionRecordMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;
    private final ArticleInformationServiceImpl articleInformationService;

    /**
     * 查询收藏夹信息
     *
     * @param id 收藏夹信息主键
     * @return 收藏夹信息
     */
    @Override
    public CollectionInformationVo queryById(Long id) {
        Long count = collectionRecordMapper.selectCount(new QueryWrapper<CollectionRecord>().eq("collection_id", id));
        CollectionInformationVo collectionInformationVo = baseMapper.selectVoById(id);
        collectionInformationVo.setIncludedCount(Integer.valueOf(count.toString()));
        return collectionInformationVo;
    }

    /**
     * 查询收藏夹信息列表
     *
     * @param bo 收藏夹信息
     * @return 收藏夹信息
     */
    @Override
    public TableDataInfo<CollectionInformationVo> queryPageList(CollectionInformationBo bo, PageQuery pageQuery) {
        QueryWrapper<CollectionInformation> lqw = buildQueryWrappers(bo);
        Page<CollectionInformationVo> result = baseMapper.selectUserName(pageQuery.build(), lqw);
        return TableDataInfo.build(result);

    }

    /**
     * 查询收藏夹信息列表
     *
     * @param bo 收藏夹信息
     * @return 收藏夹信息
     */
    @Override
    public List<CollectionInformationVo> queryList(CollectionInformationBo bo) {
        LambdaQueryWrapper<CollectionInformation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CollectionInformation> buildQueryWrapper(CollectionInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CollectionInformation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getCollectionName()), CollectionInformation::getCollectionName, bo.getCollectionName());
        lqw.eq(bo.getState() != null, CollectionInformation::getState, bo.getState());
        lqw.eq(bo.getUid() != null, CollectionInformation::getUid, bo.getUid());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            CollectionInformation::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    private QueryWrapper<CollectionInformation> buildQueryWrappers(CollectionInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<CollectionInformation> lqw = Wrappers.query();
        lqw.like(StringUtils.isNotBlank(bo.getCollectionName()), "collection_name", bo.getCollectionName());
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), "um.nickname", bo.getUsername());
        lqw.eq(bo.getState() != null, "ci.state", bo.getState());
        lqw.like(bo.getUid() != null, "uid", bo.getUid());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            "ci.create_time", params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增收藏夹信息
     *
     * @param bo 收藏夹信息
     * @return 结果
     */

    @Override
    public Boolean insertByBo(CollectionInformationBo bo) {
        CollectionInformation add = BeanUtil.toBean(bo, CollectionInformation.class);
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid() == null ? LoginHelper.getUserId().toString() : LoginHelper.getTripartiteUuid());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改收藏夹信息
     *
     * @param bo 收藏夹信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(CollectionInformationBo bo) {
        CollectionInformation update = BeanUtil.toBean(bo, CollectionInformation.class);
        update.setUpdateId(LoginHelper.getTripartiteUuid());
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除收藏夹信息
     *
     * @param ids 需要删除的收藏夹信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<CollectionInformationVo> listUid(String uid) {
        List<CollectionInformationVo> list1 = baseMapper.selectVoList(new QueryWrapper<CollectionInformation>()
            .eq("uid", uid).eq("state", 0));
        List<CollectionInformationVo> list = collectionRecordMapper.selectGroupOn(uid);
        if (CollectionUtils.isNotEmpty(list) && CollectionUtils.isNotEmpty(list)) {
            Map<Long, Integer> collect = list.stream().collect(Collectors.toMap(CollectionInformationVo::getId, CollectionInformationVo::getIncludedCount));
            list1.forEach(item -> {
                if (collect.get(item.getId()) != null) {
                    item.setIncludedCount(collect.get(item.getId()));
                }
            });

        }
        return list1;
    }

    @Override
    public List<CollectionInformationVo> list() {
        return baseMapper.selectVoList(new QueryWrapper<CollectionInformation>().eq("state", 0));
    }

    @Override
    public Page<ArticleInformationVo> articleList(CollectionRecordBo bo, PageQuery pageQuery) {
        return baseMapper.articleList(bo, pageQuery.build());
    }

    @Override
    public boolean collectionArticle(CollectionRecordBo bo) {
        CollectionRecord add = BeanUtil.toBean(bo, CollectionRecord.class);
        add.setCreateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid());
        int insert = collectionRecordMapper.insert(add);
        articleInformationService.recordArticleIntimacy(LoginHelper.getTripartiteUuid(), bo.getLabelId(), 3D);
        return insert > 0;
    }

    @Override
    public boolean deleteCollectionArticle(Long id, String labelId) {
        int i = collectionRecordMapper.deleteById(id);
        if (i > 0) {
            articleInformationService.recordArticleIntimacy(LoginHelper.getTripartiteUuid(), labelId, -3D);
        }
        return i > 0;
    }

    @Override
    public boolean collectionUpdate(CollectionRecordBo bo) {
        int update = collectionRecordMapper.update(null, new UpdateWrapper<CollectionRecord>()
            .set("collection_id", bo.getCollectionId())
            .set("update_time", new Date())
            .eq("target_id", bo.getTargetId())
            .eq("type", bo.getType())
            .eq("uid", LoginHelper.getTripartiteUuid()));
        return update > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCollection(CollectionInformationBo bo) {
        CollectionInformation add = BeanUtil.toBean(bo, CollectionInformation.class);
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        countUserWebsiteMapper.updateAdd(add.getUid(), CountUserType.COLLECTION_COUNT.getCode());
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCollection(Long id) {
        countUserWebsiteMapper.updateDelete(LoginHelper.getTripartiteUuid(), CountUserType.COLLECTION_COUNT.getCode());
        collectionRecordMapper.update(null,
            new UpdateWrapper<CollectionRecord>().set("state", 1).eq("collection_id", id));
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public List<CensusVo> timeCollectionCensus(CensusEntity bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return baseMapper.timeCollectionCensus(bo);
    }
}

