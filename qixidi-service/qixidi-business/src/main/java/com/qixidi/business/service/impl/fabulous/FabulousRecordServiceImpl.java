package com.qixidi.business.service.impl.fabulous;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.enums.CommonStatusEnums;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.service.fabulous.IFabulousRecordService;
import com.qixidi.business.service.impl.article.ArticleInformationServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * 点赞Service业务层处理
 *
 * @author aurora
 * @date 2022-10-01
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class FabulousRecordServiceImpl implements IFabulousRecordService {

    private final FabulousRecordMapper baseMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    private final ArticleInformationServiceImpl articleInformationService;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    /**
     * 查询点赞
     *
     * @param id 点赞主键
     * @return 点赞
     */
    @Override
    public FabulousRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞
     */
    @Override
    public TableDataInfo<FabulousRecordVo> queryPageList(FabulousRecordBo bo, PageQuery pageQuery) {
        //构造查询条件
        LambdaQueryWrapper<FabulousRecord> lqw = buildQueryWrapper(bo);
        //查询数据库
        Page<FabulousRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        //返回
        return TableDataInfo.build(result);
    }

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞
     */
    @Override
    public List<FabulousRecordVo> queryList(FabulousRecordBo bo) {
        LambdaQueryWrapper<FabulousRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<FabulousRecord> buildQueryWrapper(FabulousRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<FabulousRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), FabulousRecord::getUid, bo.getUid());
        lqw.eq(bo.getTargetId() != null, FabulousRecord::getTargetId, bo.getTargetId());
        lqw.eq(bo.getType() != null, FabulousRecord::getType, bo.getType());
        lqw.eq(bo.getState() != null, FabulousRecord::getState, bo.getState());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                FabulousRecord::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    @Override
    public Boolean insertByBo(FabulousRecordBo bo) {
        //转换
        FabulousRecord add = BeanUtil.toBean(bo, FabulousRecord.class);
        add.setCreateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid());

        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    @Override
    public Boolean updateByBo(FabulousRecordBo bo) {
        FabulousRecord update = BeanUtil.toBean(bo, FabulousRecord.class);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 批量删除点赞
     *
     * @param ids 需要删除的点赞主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public void spotFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("点赞开始，typeId:{}，uid:{}", bo.getTypeId(), bo.getUid());

        // 1. 写 DB
        FabulousRecord record = new FabulousRecord();
        record.setUid(bo.getUid());
        record.setTypeId(bo.getTypeId());
        record.setTargetId(bo.getTargetId());
        record.setTargetUid(bo.getTargetUid());
        record.setType(bo.getType());
        record.setState(CommonStatusEnums.NORMAL.getCode());
        record.setCreateTime(new Date());
        baseMapper.insert(record);

        // 2. 写 Redis（点赞时间戳）
        String redisKey = RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey();
        RedisUtils.setCacheMapValue(redisKey, bo.getTypeId() + ":" + bo.getUid(), System.currentTimeMillis());

        // 3. 记录文章亲密度
        articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getLabelId(), 2D);

        // 4. 通知
        if (bo.getUid().equals(bo.getTargetUid())) return;
        executorService.execute(() -> {
            NewsUserRecord newsUserRecord = new NewsUserRecord();
            newsUserRecord.setUid(bo.getTargetUid());
            newsUserRecord.setTargetId(bo.getTargetId());
            newsUserRecord.setTargetUid(bo.getUid());
            newsUserRecord.setType(NewsType.FABULOUS_NEWS.getCode());
            newsUserRecord.setContent(bo.getTargetTitle());
            newsUserRecord.setCreateTime(new Date());
            newsUserRecordMapper.insert(newsUserRecord);
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
        });
    }

    @Override
    public void cancelFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("取消点赞开始，typeId:{}，uid:{}", bo.getTypeId(), bo.getUid());

        // 1. 删 DB 记录
        baseMapper.delete(new LambdaQueryWrapper<FabulousRecord>()
                .eq(FabulousRecord::getUid, bo.getUid())
                .eq(FabulousRecord::getTypeId, bo.getTypeId())
                .eq(FabulousRecord::getType, bo.getType()));

        // 2. 删 Redis 时间戳
        String redisKey = RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey();
        RedisUtils.delCacheMapValue(redisKey, bo.getTypeId() + ":" + bo.getUid());

        // 3. 记录亲密度
        articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getLabelId(), -2D);

        // 4. 通知
        executorService.execute(() -> {
            newsUserRecordMapper.delete(new LambdaQueryWrapper<NewsUserRecord>()
                    .eq(NewsUserRecord::getTargetUid, bo.getUid())
                    .eq(NewsUserRecord::getUid, bo.getTargetUid())
                    .eq(NewsUserRecord::getType, NewsType.FABULOUS_NEWS.getCode()));
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
        });
    }

    @Override
    public TableDataInfo<ArticleInformationVo> fabulousList(UserHomeBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(baseMapper.fabulousArticleList(bo, pageQuery.build()));
    }
}
