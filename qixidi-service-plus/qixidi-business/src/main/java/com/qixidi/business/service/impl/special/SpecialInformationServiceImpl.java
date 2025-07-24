package com.qixidi.business.service.impl.special;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.MsgEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.special.SpecialInformationBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.special.SpecialInformation;
import com.qixidi.business.domain.enums.CountUserTypeEnums;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.special.SpecialInformationVo;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.service.special.ISpecialInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 专栏信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-19
 */
@RequiredArgsConstructor
@Service
public class SpecialInformationServiceImpl implements ISpecialInformationService {

    private final SpecialInformationMapper baseMapper;
    private final ArticleInformationMapper articleInformationMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;

    /**
     * 查询专栏信息
     *
     * @param id 专栏信息主键
     * @return 专栏信息
     */
    @Override
    public SpecialInformationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询专栏信息列表
     *
     * @param bo 专栏信息
     * @return 专栏信息
     */
    @Override
    public TableDataInfo<SpecialInformationVo> queryPageList(SpecialInformationBo bo, PageQuery pageQuery) {
        QueryWrapper<SpecialInformation> lqw = buildQueryWrappers(bo);
        Page<SpecialInformationVo> result = baseMapper.selectUserName(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询专栏信息列表
     *
     * @param bo 专栏信息
     * @return 专栏信息
     */
    @Override
    public List<SpecialInformationVo> queryList(SpecialInformationBo bo) {
        LambdaQueryWrapper<SpecialInformation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SpecialInformation> buildQueryWrapper(SpecialInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SpecialInformation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSpecialName()), SpecialInformation::getSpecialName, bo.getSpecialName());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecialIntroduce()), SpecialInformation::getSpecialIntroduce, bo.getSpecialIntroduce());
        lqw.eq(bo.getState() != null, SpecialInformation::getState, bo.getState());
        lqw.like(bo.getUid() != null, SpecialInformation::getUid, bo.getUid());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                SpecialInformation::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    private QueryWrapper<SpecialInformation> buildQueryWrappers(SpecialInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<SpecialInformation> lqw = Wrappers.query();
        lqw.like(StringUtils.isNotBlank(bo.getSpecialName()), "special_name", bo.getSpecialName());
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), "um.nickname", bo.getUsername());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecialIntroduce()), "special_introduce", bo.getSpecialIntroduce());
        lqw.eq(bo.getState() != null, "state", bo.getState());
        lqw.like(bo.getUid() != null, "uid", bo.getUid());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                "update_time", params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(SpecialInformationBo bo) {
        SpecialInformation add = BeanUtil.toBean(bo, SpecialInformation.class);
        validEntityBeforeSave(add);
//        同步用户数据
        countUserWebsiteMapper.updateAdd(add.getUid(), CountUserTypeEnums.SPECIAL_COLUMN_COUNT.getCode());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    @Override

    public Boolean updateByBo(SpecialInformationBo bo) {
        SpecialInformation update = BeanUtil.toBean(bo, SpecialInformation.class);
        validEntityBeforeSave(update);

        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(SpecialInformation entity) {
        entity.setUid(LoginHelper.getTripartiteUuid());
        entity.setCreateTime(new Date());
    }

    /**
     * 批量删除专栏信息
     *
     * @param ids 需要删除的专栏信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<SpecialInformationVo> specialListUid(SpecialInformationBo bo) {
        return baseMapper.selectVoList(new LambdaQueryWrapper<SpecialInformation>().eq(true, SpecialInformation::getUid, bo.getUid())
                .like(StringUtils.isNotBlank(bo.getSpecialName()), SpecialInformation::getSpecialName, bo.getSpecialName())
                .orderByDesc(SpecialInformation::getCreateTime));
    }

    @Override
    public List<SpecialInformationVo> specialList() {
        String uuid = LoginHelper.getTripartiteUuid();
        return baseMapper.selectVoList(new LambdaQueryWrapper<SpecialInformation>()
                .eq(SpecialInformation::getUid, uuid)
                .eq(SpecialInformation::getState, 0)
                .orderByDesc(SpecialInformation::getCreateTime));
    }

    @Override
    public List<ArticleInformationVo> selectSpecial(SpecialInformationBo bo, PageQuery pageQuery) {
        return articleInformationMapper.selectSpecial(bo.getId(), bo.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSpecial(Long id, String uid, List<String> ids) {
        int update = articleInformationMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                .set(ArticleInformation::getSpecialId, null)
                .eq(ArticleInformation::getSpecialId, id)
                .eq(ArticleInformation::getUserId, uid));
        if (CollectionUtils.isEmpty(ids)) return update;
        int integer = articleInformationMapper.updateSpecial(ids, id);
        return integer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R remove(Long id, boolean b) throws Exception {
        String uuid = LoginHelper.getTripartiteUuid();
        List<ArticleInformationVo> list = articleInformationMapper.selectSpecial(id, uuid);
        if (CollectionUtils.isNotEmpty(list)) throw new Exception(MsgEnums.SPECIAL_CONDITION_ERROR.getValue());
        //        同步用户数据
        countUserWebsiteMapper.updateDelete(uuid, CountUserTypeEnums.SPECIAL_COLUMN_COUNT.getCode());
        boolean bs = baseMapper.deleteById(id) > 0;
        if (!bs) throw new Exception(MsgEnums.DELETE_ERROR.getValue());
        return R.ok();
    }

    @Override
    public List<CensusVo> timeSpecialCensus(CensusEntity bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return baseMapper.timeSpecialCensus(bo);
    }

    @Override
    public CountUserWebsiteVo CountUserCensus(CensusEntity bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        CountUserWebsiteVo countUserWebsiteVo = countUserWebsiteMapper.selectCensus(bo.getUid());
        return countUserWebsiteVo;
    }

    @Override
    public Page<ArticleInformationVo> getArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        Page<ArticleInformationVo> articleInfo = articleInformationMapper.getArticleInfo(bo, pageQuery.build());
        return articleInfo;
    }

    @Override
    public List<CensusVo> submissionCensus() {
        LocalDate currentDate = LocalDate.now();
        // 减去一年
        LocalDate oneYearAgo = currentDate.minusYears(1);
        // 格式化输出结果（可选）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = oneYearAgo.format(formatter);
        String uuid = LoginHelper.getTripartiteUuid();
        return articleInformationMapper.submissionCensus(uuid, formattedDate);
    }

}

