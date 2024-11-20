package com.aurora.business.service.impl.news;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.news.NewsSystemInfoBo;
import com.aurora.business.domain.entity.news.NewsSystemInfo;
import com.aurora.business.domain.vo.news.NewsSystemInfoVo;
import com.aurora.business.mapper.news.NewsSystemInfoMapper;
import com.aurora.business.service.news.INewsSystemInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统消息Service业务层处理
 *
 * @author aurora
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class NewsSystemInfoServiceImpl implements INewsSystemInfoService {

    private final NewsSystemInfoMapper baseMapper;

    /**
     * 查询系统消息
     *
     * @param id 系统消息主键
     * @return 系统消息
     */
    @Override
    public NewsSystemInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public TableDataInfo<NewsSystemInfoVo> queryPageList(NewsSystemInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NewsSystemInfo> lqw = buildQueryWrapper(bo);
        Page<NewsSystemInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public List<NewsSystemInfoVo> queryList(NewsSystemInfoBo bo) {
        LambdaQueryWrapper<NewsSystemInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<NewsSystemInfo> buildQueryWrapper(NewsSystemInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NewsSystemInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNewsTitle()), NewsSystemInfo::getNewsTitle, bo.getNewsTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getNewsContent()), NewsSystemInfo::getNewsContent, bo.getNewsContent());
        lqw.eq(bo.getIsDetails() != null, NewsSystemInfo::getIsDetails, bo.getIsDetails());
        lqw.eq(bo.getType() != null, NewsSystemInfo::getType, bo.getType());
        lqw.eq(bo.getLapseTime() != null, NewsSystemInfo::getLapseTime, bo.getLapseTime());
        return lqw;
    }

    /**
     * 新增系统消息
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(NewsSystemInfoBo bo) {
        NewsSystemInfo add = BeanUtil.toBean(bo, NewsSystemInfo.class);
        add.setCreateTime(new Date());
//        存入redis
        depositNewsRedis(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }
    public void depositNewsRedis(NewsSystemInfo newsSystemInfo) {

    }

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(NewsSystemInfoBo bo) {
        NewsSystemInfo update = BeanUtil.toBean(bo, NewsSystemInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除系统消息
     *
     * @param ids 需要删除的系统消息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
