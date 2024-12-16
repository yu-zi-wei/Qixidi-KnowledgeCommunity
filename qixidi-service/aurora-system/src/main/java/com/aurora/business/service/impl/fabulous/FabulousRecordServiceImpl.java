package com.aurora.business.service.impl.fabulous;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.fabulous.FabulousRecordBo;
import com.aurora.business.domain.bo.user.UserHomeBo;
import com.aurora.business.domain.entity.fabulous.FabulousRecord;
import com.aurora.business.domain.entity.news.NewsUserRecord;
import com.aurora.business.domain.vo.fabulous.FabulousRecordVo;
import com.aurora.business.mapper.comment.NewsUserRecordMapper;
import com.aurora.business.mapper.fabulous.FabulousRecordMapper;
import com.aurora.business.selector.webSocket.WebSocketSelector;
import com.aurora.business.service.fabulous.IFabulousRecordService;
import com.aurora.business.service.impl.article.ArticleInformationServiceImpl;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.CommentType;
import com.aurora.common.enums.RedisKeyEnums;
import com.aurora.common.enums.WebSocketEnum;
import com.aurora.common.enums.news.NewsType;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.redis.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public R spotFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("点赞数据存入redis开始，articleId:{}，uid:{}，FabulousSum:{}", bo.getType(), bo.getUid(), bo.getFabulousSum());
        synchronized (this) {
            //            文章点赞总数加一
            Map<String, Object> sumMap = new HashMap();
            sumMap.put(bo.getTypeId().toString(), bo.getFabulousSum() + 1);
            RedisUtils.setCacheMapValue(RedisKeyEnums.TOTAL_LIKE_COUNT_KEY.getKey(), bo.getTypeId().toString(), bo.getFabulousSum() + 1);
            //            我的点赞文章加一
            Map<String, Set<String>> cacheMpa = new HashMap();
            if (RedisUtils.hasKey(RedisKeyEnums.USER_LIKE_ARTICLE_KEY.getKey())) {
                cacheMpa = RedisUtils.getCacheMap(RedisKeyEnums.USER_LIKE_ARTICLE_KEY.getKey());
            }
            Set<String> userFuSet = CollectionUtils.isEmpty(cacheMpa.get(bo.getUid())) ? new HashSet()
                : cacheMpa.get(bo.getUid());
            userFuSet.add(bo.getTypeId().toString());
            cacheMpa.put(bo.getUid(), userFuSet);
            RedisUtils.setCacheMap(RedisKeyEnums.USER_LIKE_ARTICLE_KEY.getKey(), cacheMpa);

            //文章点赞用户列表加1
            Map<String, Set<String>> articleMap = new HashMap();
            if (RedisUtils.hasKey(RedisKeyEnums.ARTICLE_LIKED_USER_KEY.getKey())) {
                articleMap = RedisUtils.getCacheMap(RedisKeyEnums.ARTICLE_LIKED_USER_KEY.getKey());
            }
            Set<String> articleSet = CollectionUtils.isEmpty(articleMap.get(bo.getTypeId().toString())) ? new HashSet()
                : articleMap.get(bo.getTypeId().toString());
            articleSet.add(bo.getUid());
            articleMap.put(bo.getTypeId().toString(), articleSet);
            RedisUtils.setCacheMap(RedisKeyEnums.ARTICLE_LIKED_USER_KEY.getKey(), articleMap);
            //记录文章亲密度
            articleInformationService.recordArticleIntimacy(bo.getUid(),bo.getLabelId(),2D);
        }
        if (bo.getUid().equals(bo.getTargetUid())) return R.ok();
//        发送消息
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                NewsUserRecord newsUserRecord = new NewsUserRecord();
                newsUserRecord.setUid(bo.getTargetUid());
                newsUserRecord.setTargetId(bo.getTargetId());
                newsUserRecord.setTargetUid(bo.getUid());
                newsUserRecord.setType(NewsType.FABULOUS_NEWS.getCode());
                newsUserRecord.setContent(bo.getTargetTitle());
                newsUserRecord.setCreateTime(new Date());
                newsUserRecordMapper.insert(newsUserRecord);
                //WebSocket推送消息
                WebSocketSelector.execute(bo.getTargetUid(), WebSocketEnum.INSIDE_NOTICE);
            }
        });
        return R.ok();
    }

    @Override
    public R<Void> cancelFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("取消点赞开始，articleId:{}，uid:{}，FabulousSum:{}", bo.getType(), bo.getUid(), bo.getFabulousSum());
        synchronized (this) {
            //            文章点赞总数减一
            Map<String, Object> mapCount = new HashMap();
            mapCount.put(bo.getTypeId().toString(), bo.getFabulousSum() - 1);
            RedisUtils.setCacheMap(RedisKeyEnums.TOTAL_LIKE_COUNT_KEY.getKey(), mapCount);
            //            用户点赞的文章减一
            Map<String, Set<String>> cacheMpa = RedisUtils.getCacheMap(RedisKeyEnums.USER_LIKE_ARTICLE_KEY.getKey());
            Set<String> userFuSet = cacheMpa.get(bo.getUid());
            userFuSet.remove(bo.getTypeId().toString());
            cacheMpa.put(bo.getUid(), userFuSet);
            RedisUtils.setCacheMap(RedisKeyEnums.USER_LIKE_ARTICLE_KEY.getKey(), cacheMpa);

            //文章点赞人加一
            Map<String, Set<String>> articleMap = RedisUtils.getCacheMap(RedisKeyEnums.ARTICLE_LIKED_USER_KEY.getKey());
            Set<String> articleSet = articleMap.get(bo.getTypeId().toString());
            articleSet.remove(bo.getUid());
            articleMap.put(bo.getTypeId().toString(), articleSet);
            RedisUtils.setCacheMap(RedisKeyEnums.ARTICLE_LIKED_USER_KEY.getKey(), articleMap);
            //记录亲密度
            articleInformationService.recordArticleIntimacy(bo.getUid(),bo.getLabelId(),-2D);
            //发送消息
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    newsUserRecordMapper.delete(new QueryWrapper<NewsUserRecord>()
                        .eq("target_uid", bo.getUid())
                        .eq("uid", bo.getTargetUid())
                        .eq("type", NewsType.FABULOUS_NEWS.getCode()));
                    //WebSocket推送消息
                    WebSocketSelector.execute(bo.getTargetUid(), WebSocketEnum.INSIDE_NOTICE);
                }
            });
        }
        log.info("取消点赞结束");
        return null;
    }

    @Override
    public Object fabulousList(UserHomeBo bo, PageQuery pageQuery) {
        if (bo.getType().equals(CommentType.ARTICLE_TYPE.getCode()))
            return baseMapper.fabulousArticleList(bo, pageQuery.build());
//        if (bo.getType().equals(CommentType.COMMENT_TYPE.getCode())) return baseMapper.fabulousCommentList(bo);
        return null;
    }
}
