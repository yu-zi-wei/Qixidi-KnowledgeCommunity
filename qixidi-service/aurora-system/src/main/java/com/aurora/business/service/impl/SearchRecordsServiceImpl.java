package com.aurora.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.SearchRecordsBo;
import com.aurora.business.domain.entity.SearchRecords;
import com.aurora.business.domain.vo.SearchRecordsVo;
import com.aurora.business.mapper.SearchRecordsMapper;
import com.aurora.business.service.ISearchRecordsService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 搜索记录Service业务层处理
 *
 * @author aurora
 * @date 2023-04-18
 */
@RequiredArgsConstructor
@Service
public class SearchRecordsServiceImpl implements ISearchRecordsService {

    private final SearchRecordsMapper baseMapper;

    /**
     * 查询搜索记录
     *
     * @param id 搜索记录主键
     * @return 搜索记录
     */
    @Override
    public SearchRecordsVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询搜索记录列表
     *
     * @param bo 搜索记录
     * @return 搜索记录
     */
    @Override
    public TableDataInfo<SearchRecordsVo> queryPageList(SearchRecordsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SearchRecords> lqw = buildQueryWrapper(bo);
        Page<SearchRecordsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询搜索记录列表
     *
     * @param bo 搜索记录
     * @return 搜索记录
     */
    @Override
    public List<SearchRecordsVo> queryList(SearchRecordsBo bo) {
        LambdaQueryWrapper<SearchRecords> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SearchRecords> buildQueryWrapper(SearchRecordsBo bo) {
        LambdaQueryWrapper<SearchRecords> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), SearchRecords::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), SearchRecords::getContent, bo.getContent());
        return lqw;
    }

    /**
     * 新增搜索记录
     *
     * @param bo 搜索记录
     * @return 结果
     */
    @Override
    public Boolean insertByBo(SearchRecordsBo bo) {
        SearchRecords add = BeanUtil.toBean(bo, SearchRecords.class);
        add.setCreateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改搜索记录
     *
     * @param bo 搜索记录
     * @return 结果
     */
    @Override
    public Boolean updateByBo(SearchRecordsBo bo) {
        SearchRecords update = BeanUtil.toBean(bo, SearchRecords.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除搜索记录
     *
     * @param ids 需要删除的搜索记录主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<SearchRecordsVo> frontDeskList(SearchRecordsBo bo, PageQuery pageQuery) {
        IPage<SearchRecordsVo> iPage = baseMapper.selectContent(bo, pageQuery.build());
        return TableDataInfo.build(iPage.getRecords());
    }
}

