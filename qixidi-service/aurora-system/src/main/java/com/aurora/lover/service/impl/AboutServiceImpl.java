/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:22
 * @version 1.0
 */
package com.aurora.lover.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.About;
import com.aurora.lover.domain.bo.AboutBo;
import com.aurora.lover.domain.vo.AboutVo;
import com.aurora.lover.mapper.AboutMapper;
import com.aurora.lover.service.IAboutService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 关于我们Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-30
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class AboutServiceImpl implements IAboutService {

    private final AboutMapper baseMapper;

    /**
     * 查询关于我们
     */
    @Override
    public AboutVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询关于我们列表
     */
    @Override
    public TableDataInfo<AboutVo> queryPageList(AboutBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<About> lqw = buildQueryWrapper(bo);
        lqw.select(About.class, f -> !f.getColumn().equals("content"));
        Page<AboutVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询关于我们列表
     */
    @Override
    public List<AboutVo> queryList(AboutBo bo) {
        LambdaQueryWrapper<About> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<About> buildQueryWrapper(AboutBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<About> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), About::getContent, bo.getContent());
        lqw.eq(bo.getState() != null, About::getState, bo.getState());
        lqw.like(bo.getContent() != null, About::getContent, bo.getContent());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            About::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增关于我们
     */
    @Override
    public Boolean insertByBo(AboutBo bo) {
        About add = BeanUtil.toBean(bo, About.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改关于我们
     */
    @Override
    public Boolean updateByBo(AboutBo bo) {
        About update = BeanUtil.toBean(bo, About.class);
        return baseMapper.updateById(update) > 0;
    }
    /**
     * 批量删除关于我们
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public AboutVo aboutApi() {
        return baseMapper.aboutApi();
    }
}
