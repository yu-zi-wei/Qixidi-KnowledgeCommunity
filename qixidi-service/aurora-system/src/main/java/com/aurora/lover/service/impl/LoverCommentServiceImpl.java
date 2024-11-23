/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:32
 * @version 1.0
 */
package com.aurora.lover.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.LoverMail;
import com.aurora.common.utils.email.MailUtils;
import com.aurora.lover.domain.LoverComment;
import com.aurora.lover.domain.bo.LoverCommentBo;
import com.aurora.lover.domain.vo.LoverCommentVo;
import com.aurora.lover.mapper.LoverCommentMapper;
import com.aurora.lover.service.ILoverCommentService;
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
 * 爱情祝福语Service业务层处理
 *
 * @author ziwei
 * @date 2022-11-30
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class LoverCommentServiceImpl implements ILoverCommentService {

    private final LoverCommentMapper baseMapper;

    /**
     * 查询爱情祝福语
     */
    @Override
    public LoverCommentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询爱情祝福语列表
     */
    @Override
    public TableDataInfo<LoverCommentVo> queryPageList(LoverCommentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverComment> lqw = buildQueryWrapper(bo);
        Page<LoverCommentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询爱情祝福语列表
     */
    @Override
    public List<LoverCommentVo> queryList(LoverCommentBo bo) {
        LambdaQueryWrapper<LoverComment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LoverComment> buildQueryWrapper(LoverCommentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LoverComment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), LoverComment::getAvatar, bo.getAvatar());
        lqw.like(StringUtils.isNotBlank(bo.getName()), LoverComment::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), LoverComment::getContent, bo.getContent());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            LoverComment::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(LoverComment::getId);
        return lqw;
    }

    /**
     * 新增爱情祝福语
     */
    @Override
    public Boolean insertByBo(LoverCommentBo bo) {
        LoverComment add = BeanUtil.toBean(bo, LoverComment.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改爱情祝福语
     */
    @Override
    public Boolean updateByBo(LoverCommentBo bo) {
        LoverComment update = BeanUtil.toBean(bo, LoverComment.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除爱情祝福语
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Integer insert(LoverCommentBo bo) {
        LoverComment add = BeanUtil.toBean(bo, LoverComment.class);
        int insert = baseMapper.insert(add);
//        发送邮件
        StringBuffer content = new StringBuffer();
        content.append(" <div>");
        content.append("<h2 style=\"color: #8e44ad\">" + bo.getContent() + "</h2>");
        content.append("<hr/>");
        content.append("<span style=\"font-weight: bold\">祝福人：</span>");
        content.append(" <span style=\"color: #8e44ad;\">" + bo.getName() + "</span>");
        content.append(" <span style=\"font-weight: bold\">时间：</span>");
        content.append("<span style=\"color: #8e44ad;\">" + DateUtil.now() + "</span>");
        content.append("</div>");
        MailUtils.sendHtml(LoverMail.MailData.getMail(), "祝福语", content.toString());
        return insert;
    }
}
