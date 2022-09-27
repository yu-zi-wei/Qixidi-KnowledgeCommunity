package com.aurora.business.service.impl.article;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.mapper.article.ArticleInformationMapper;
import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-16
 */
@RequiredArgsConstructor
@Service
public class ArticleInformationServiceImpl implements IArticleInformationService {

    private final ArticleInformationMapper baseMapper;

    /**
     * 查询文章信息
     *
     * @param id 文章信息主键
     * @return 文章信息
     */
    @Override
    public ArticleInformationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询文章信息列表
     *
     * @param bo 文章信息
     * @return 文章信息
     */
    @Override
    public List<ArticleInformationVo> queryList(ArticleInformationBo bo) {
        QueryWrapper<ArticleInformation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private QueryWrapper<ArticleInformation> buildQueryWrapper(ArticleInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<ArticleInformation> lqw = Wrappers.query();
        lqw.like(StringUtils.isNotBlank(bo.getArticleTitle()), "ai.article_title", bo.getArticleTitle());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            "ai.create_time", params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc("ai.create_time");
        return lqw;
    }

    /**
     * 新增文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ArticleInformationBo bo) {
        ArticleInformation add = BeanUtil.toBean(bo, ArticleInformation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ArticleInformationBo bo) {
        ArticleInformation update = BeanUtil.toBean(bo, ArticleInformation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(ArticleInformation entity) {
        String uuid = LoginHelper.getTripartiteUser().getUuid();
        entity.setUserId(Long.valueOf(uuid));
        entity.setCreateTime(new Date());
    }

    /**
     * 批量删除文章信息
     *
     * @param ids 需要删除的文章信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<ArticleInformationVo> index(ArticleInformationBo bo, PageQuery pageQuery) {
        QueryWrapper<ArticleInformation> wrapper = buildQueryWrapper(bo);
        IPage<ArticleInformationVo> index = baseMapper.selectIndex(pageQuery.build(), wrapper);
        System.out.println(index);
        return TableDataInfo.build(index);
    }

    @Override
    public List<ArticleInformationVo> sortIndex(SortTypeBo bo) {
        return baseMapper.selectTypeSort(bo);
    }

    @Override
    public List<ArticleInformationVo> articleList(ArticleInformationBo bo, PageQuery pageQuery) {
        pageQuery.setPageNum((pageQuery.getPageNum() - 1) * pageQuery.getPageSize());
        return baseMapper.articleList(bo, pageQuery);
    }

    @Override
    public ArticleInformationVo details(Long id) {
        return baseMapper.details(id);
    }

    @Override
    public List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery) {
        return baseMapper.relatedList(bo, pageQuery);
    }
}


