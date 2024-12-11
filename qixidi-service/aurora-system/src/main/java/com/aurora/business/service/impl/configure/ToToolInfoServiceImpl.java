package com.aurora.business.service.impl.configure;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.configure.ToToolInfoBo;
import com.aurora.business.domain.entity.configure.ToToolInfo;
import com.aurora.business.domain.vo.configure.ToToolInfoVo;
import com.aurora.business.mapper.configure.ToToolInfoMapper;
import com.aurora.business.service.configure.IToToolInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 工具信息Service业务层处理
 *
 * @author aurora
 * @date 2022-10-21
 */
@RequiredArgsConstructor
@Service
public class ToToolInfoServiceImpl implements IToToolInfoService {

    private final ToToolInfoMapper baseMapper;

    /**
     * 查询工具信息
     *
     * @param id 工具信息主键
     * @return 工具信息
     */
    @Override
    public ToToolInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询工具信息列表
     *
     * @param bo 工具信息
     * @return 工具信息
     */
    @Override
    public Map<String, Object> queryPageList(ToToolInfoBo bo, PageQuery pageQuery) {
        Map<String, Object> map = new HashMap();
        LambdaQueryWrapper<ToToolInfo> lqw = buildQueryWrapper(bo);
        Page<ToToolInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<ToToolInfoVo> toToolInfoVos = baseMapper.selectVoList(new QueryWrapper<ToToolInfo>()
            .eq("is_parent", 1).eq("state", 0));
        map.put("parentFalse", result);
        map.put("parentTrue", toToolInfoVos);
        return map;
    }

    /**
     * 查询工具信息列表
     *
     * @param bo 工具信息
     * @return 工具信息
     */
    @Override
    public List<ToToolInfoVo> queryList(ToToolInfoBo bo) {
        LambdaQueryWrapper<ToToolInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToToolInfo> buildQueryWrapper(ToToolInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ToToolInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getParentId() != null, ToToolInfo::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getToolName()), ToToolInfo::getToolName, bo.getToolName());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), ToToolInfo::getIcon, bo.getIcon());
        lqw.eq(bo.getIsParent() != null, ToToolInfo::getIsParent, bo.getIsParent());
        lqw.eq(bo.getOrder() != null, ToToolInfo::getOrder, bo.getOrder());
        lqw.eq(bo.getType() != null, ToToolInfo::getType, bo.getType());
        lqw.eq(bo.getState() != null, ToToolInfo::getState, bo.getState());
        lqw.orderByDesc(ToToolInfo::getCreateTime);
        return lqw;
    }

    /**
     * 新增工具信息
     *
     * @param bo 工具信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToToolInfoBo bo) {
        ToToolInfo add = BeanUtil.toBean(bo, ToToolInfo.class);
        add.setCreateBy(LoginHelper.getUsername());
        add.setCreateTime(new Date());
        add.setType(1L);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改工具信息
     *
     * @param bo 工具信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToToolInfoBo bo) {
        ToToolInfo update = BeanUtil.toBean(bo, ToToolInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除工具信息
     *
     * @param ids 需要删除的工具信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<ToToolInfoVo> fdList() {
        return baseMapper.selectListSlq();
    }

    @Override
    public List<ToToolInfoVo> childList(ToToolInfoBo bo) {
        LambdaQueryWrapper<ToToolInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, ToToolInfo::getParentId, bo.getId());
        lqw.eq(bo.getIsParent() != null, ToToolInfo::getIsParent, bo.getIsParent());
        lqw.like(StringUtils.isNotBlank(bo.getToolName()), ToToolInfo::getToolName, bo.getToolName());
        lqw.or().like(StringUtils.isNotBlank(bo.getToolName()), ToToolInfo::getDescribe, bo.getToolName());
        lqw.orderByDesc(ToToolInfo::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }
}

