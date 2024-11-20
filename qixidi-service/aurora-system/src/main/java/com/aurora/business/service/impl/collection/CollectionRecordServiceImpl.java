package com.aurora.business.service.impl.collection;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.collection.CollectionRecordBo;
import com.aurora.business.domain.entity.collection.CollectionRecord;
import com.aurora.business.domain.vo.collection.CollectionRecordVo;
import com.aurora.business.mapper.collection.CollectionRecordMapper;
import com.aurora.business.service.collection.ICollectionRecordService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 文章收藏关联Service业务层处理
 *
 * @author aurora
 * @date 2022-11-10
 */
@RequiredArgsConstructor
@Service
public class CollectionRecordServiceImpl implements ICollectionRecordService {

    private final CollectionRecordMapper baseMapper;

    /**
     * 查询文章收藏关联
     *
     * @param id 文章收藏关联主键
     * @return 文章收藏关联
     */
    @Override
    public CollectionRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文章收藏关联列表
     *
     * @param bo 文章收藏关联
     * @return 文章收藏关联
     */
    @Override
    public TableDataInfo<CollectionRecordVo> queryPageList(CollectionRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<CollectionRecord> lqw = buildQueryWrapper(bo);
        Page<CollectionRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文章收藏关联列表
     *
     * @param bo 文章收藏关联
     * @return 文章收藏关联
     */
    @Override
    public List<CollectionRecordVo> queryList(CollectionRecordBo bo) {
        LambdaQueryWrapper<CollectionRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CollectionRecord> buildQueryWrapper(CollectionRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CollectionRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getTargetId() != null, CollectionRecord::getTargetId, bo.getTargetId());
        lqw.eq(bo.getCollectionId() != null, CollectionRecord::getCollectionId, bo.getCollectionId());
        lqw.eq(bo.getType() != null, CollectionRecord::getType, bo.getType());
        lqw.eq(bo.getState() != null, CollectionRecord::getState, bo.getState());
        lqw.eq(bo.getUid() != null, CollectionRecord::getUid, bo.getUid());
        return lqw;
    }

    /**
     * 新增文章收藏关联
     *
     * @param bo 文章收藏关联
     * @return 结果
     */
    @Override
    public Boolean insertByBo(CollectionRecordBo bo) {
        CollectionRecord add = BeanUtil.toBean(bo, CollectionRecord.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文章收藏关联
     *
     * @param bo 文章收藏关联
     * @return 结果
     */
    @Override
    public Boolean updateByBo(CollectionRecordBo bo) {
        CollectionRecord update = BeanUtil.toBean(bo, CollectionRecord.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除文章收藏关联
     *
     * @param ids 需要删除的文章收藏关联主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
