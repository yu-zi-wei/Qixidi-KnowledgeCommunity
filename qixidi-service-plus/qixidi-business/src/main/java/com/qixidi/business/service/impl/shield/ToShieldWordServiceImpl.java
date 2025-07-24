package com.qixidi.business.service.impl.shield;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.core.utils.word.WordFilter;
import com.light.redission.utils.RedisUtils;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.shield.ToShieldWordBo;
import com.qixidi.business.domain.entity.shield.ToShieldWord;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.vo.shield.ToShieldWordVo;
import com.qixidi.business.mapper.shield.ToShieldWordMapper;
import com.qixidi.business.service.shield.IToShieldWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 屏蔽词库Service业务层处理
 *
 * @author aurora
 * @date 2023-02-20
 */
@RequiredArgsConstructor
@Service
public class ToShieldWordServiceImpl implements IToShieldWordService {

    private final ToShieldWordMapper baseMapper;

    /**
     * 查询屏蔽词库
     *
     * @param id 屏蔽词库主键
     * @return 屏蔽词库
     */
    @Override
    public ToShieldWordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询屏蔽词库列表
     *
     * @param bo 屏蔽词库
     * @return 屏蔽词库
     */
    @Override
    public TableDataInfo<ToShieldWordVo> queryPageList(ToShieldWordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToShieldWord> lqw = buildQueryWrapper(bo);
        Page<ToShieldWordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询屏蔽词库列表
     *
     * @param bo 屏蔽词库
     * @return 屏蔽词库
     */
    @Override
    public List<ToShieldWordVo> queryList(ToShieldWordBo bo) {
        LambdaQueryWrapper<ToShieldWord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToShieldWord> buildQueryWrapper(ToShieldWordBo bo) {
        LambdaQueryWrapper<ToShieldWord> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getKeyword()), ToShieldWord::getKeyword, bo.getKeyword());
        lqw.eq(bo.getType() != null, ToShieldWord::getType, bo.getType());
        lqw.eq(bo.getWeight() != null, ToShieldWord::getWeight, bo.getWeight());
        lqw.eq(bo.getState() != null, ToShieldWord::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增屏蔽词库
     *
     * @param bo 屏蔽词库
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToShieldWordBo bo) {
        ToShieldWord add = BeanUtil.toBean(bo, ToShieldWord.class);
        add.setCreateTime(new Date());
        add.setCreateBy(LoginHelper.getUsername());

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改屏蔽词库
     *
     * @param bo 屏蔽词库
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToShieldWordBo bo) {
        ToShieldWord update = BeanUtil.toBean(bo, ToShieldWord.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除屏蔽词库
     *
     * @param ids 需要删除的屏蔽词库主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 屏蔽词缓存
     */
    @Override
    public void ShieldWordRefresh() {
        List<String> list = baseMapper.selectKeyword();
//        存入缓存
        RedisUtils.deleteObject(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey());
        RedisUtils.setCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey(), list);
    }

    @Override
    public Map<String, Object> detection(String text) {
        List<String> list = RedisUtils.getCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey());
        if (CollectionUtils.isEmpty(list)) {
            ShieldWordRefresh();
            return null;
        }
//        存入缓存
        WordFilter wordFilter = new WordFilter(list);

        Map<String, Object> map = new HashMap<>();
        Set<String> strings = wordFilter.wordList(text);
        int i = wordFilter.wordCount(text);
        map.put("敏感词：", strings);
        map.put("命中次数", i);
        return map;
    }
}
