package com.qixidi.business.service.impl.configure;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.configure.ToToolInfoBo;
import com.qixidi.business.domain.entity.configure.ToToolInfo;
import com.qixidi.business.domain.vo.configure.ToToolInfoVo;
import com.qixidi.business.mapper.configure.ToToolInfoMapper;
import com.qixidi.business.service.configure.IToToolInfoService;
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
     * 查询工具信息列表（后台平铺分页，父子层级由 isParent 字段区分）
     *
     * @param bo 工具信息
     * @return 工具信息
     */
    @Override
    public TableDataInfo<ToToolInfoVo> queryPageList(ToToolInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToToolInfo> lqw = buildQueryWrapper(bo);
        Page<ToToolInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
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
        lqw.in(bo.getStates() != null && !bo.getStates().isEmpty(), ToToolInfo::getState, bo.getStates());
        lqw.in(bo.getIsParents() != null && !bo.getIsParents().isEmpty(), ToToolInfo::getIsParent, bo.getIsParents());
        lqw.in(bo.getParentIds() != null && !bo.getParentIds().isEmpty(), ToToolInfo::getParentId, bo.getParentIds());
        lqw.orderByDesc(ToToolInfo::getCreateTime);
        return lqw;
    }

    /**
     * 目录/子级分流校验与归一：目录（isParent=1）无地址（置空串，null 会被非 null 更新策略忽略）；子级（isParent=2）必须填写地址与所属目录
     */
    private void validateAndNormalize(ToToolInfoBo bo) {
        if (bo.getIsParent() == null) {
            throw new ServiceException("层级（是否为父级）不能为空");
        }
        if (bo.getIsParent() == 1) {
            bo.setParentId(null);
            bo.setToolUrl("");
            return;
        }
        if (StringUtils.isBlank(bo.getToolUrl())) {
            throw new ServiceException("子级工具必须填写工具地址");
        }
        if (bo.getParentId() == null) {
            throw new ServiceException("子级工具必须选择所属目录");
        }
    }

    /**
     * 新增工具信息
     *
     * @param bo 工具信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToToolInfoBo bo) {
        validateAndNormalize(bo);
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
        validateAndNormalize(bo);
        ToToolInfo update = BeanUtil.toBean(bo, ToToolInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除工具信息：目录（isParent=1）下仍有子级时禁止删除，避免前台出现孤儿工具
     *
     * @param ids 需要删除的工具信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            List<ToToolInfo> rows = baseMapper.selectBatchIds(ids);
            List<Long> catalogIds = rows.stream()
                .filter(r -> r.getIsParent() != null && r.getIsParent() == 1)
                .map(ToToolInfo::getId)
                .toList();
            if (!catalogIds.isEmpty()) {
                Long childCount = baseMapper.selectCount(
                    Wrappers.lambdaQuery(ToToolInfo.class).in(ToToolInfo::getParentId, catalogIds));
                if (childCount > 0) {
                    throw new ServiceException("所选目录下仍有工具，请先移除或删除目录下的工具");
                }
            }
        }
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

