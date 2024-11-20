package com.aurora.business.service.impl.configure;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.configure.ToSiteFileBo;
import com.aurora.business.domain.entity.configure.ToSiteFile;
import com.aurora.business.domain.vo.configure.ToSiteFileVo;
import com.aurora.business.mapper.configure.ToSiteFileMapper;
import com.aurora.business.service.configure.IToSiteFileService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.ToSiteFileType;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.aurora.system.domain.SysOss;
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
 * 网站文档Service业务层处理
 *
 * @author aurora
 * @date 2022-10-21
 */
@RequiredArgsConstructor
@Service
public class ToSiteFileServiceImpl implements IToSiteFileService {

    private final ToSiteFileMapper baseMapper;

    /**
     * 查询网站文档
     *
     * @param id 网站文档主键
     * @return 网站文档
     */
    @Override
    public ToSiteFileVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询网站文档列表
     *
     * @param bo 网站文档
     * @return 网站文档
     */
    @Override
    public TableDataInfo<ToSiteFileVo> queryPageList(ToSiteFileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToSiteFile> lqw = buildQueryWrapper(bo);
        Page<ToSiteFileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询网站文档列表
     *
     * @param bo 网站文档
     * @return 网站文档
     */
    @Override
    public List<ToSiteFileVo> queryList(ToSiteFileBo bo) {
        LambdaQueryWrapper<ToSiteFile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToSiteFile> buildQueryWrapper(ToSiteFileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ToSiteFile> lqw = Wrappers.lambdaQuery(ToSiteFile.class);
        lqw.select(ToSiteFile.class, i -> !i.getColumn().equals("content_md") && !i.getColumn().equals("content_html"));
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), ToSiteFile::getTitle, bo.getTitle());
        lqw.eq(bo.getState() != null, ToSiteFile::getState, bo.getState());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            ToSiteFile::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.like(StringUtils.isNotBlank(bo.getType()), ToSiteFile::getType, bo.getType());
        return lqw;
    }

    /**
     * 新增网站文档
     *
     * @param bo 网站文档
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToSiteFileBo bo) {
        ToSiteFile add = BeanUtil.toBean(bo, ToSiteFile.class);
        add.setUid(LoginHelper.getUserId());
        add.setCreateTime(new Date());
        add.setType(ToSiteFileType.SITE_DOCUMENTATION.getVersion());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改网站文档
     *
     * @param bo 网站文档
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToSiteFileBo bo) {
        ToSiteFile update = BeanUtil.toBean(bo, ToSiteFile.class);
        update.setCreateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除网站文档
     *
     * @param ids 需要删除的网站文档主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

