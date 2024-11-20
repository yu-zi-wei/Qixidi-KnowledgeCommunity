package com.aurora.business.service.news;

import com.aurora.business.domain.bo.news.NewsUserInfoBo;
import com.aurora.business.domain.entity.news.NewsUserRecord;
import com.aurora.business.domain.vo.news.NewsUserInfoVo;
import com.aurora.business.domain.vo.news.NewsUserSumVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 用户消息Service接口
 *
 * @author aurora
 * @date 2022-11-03
 */
@Service
public interface INewsUserInfoService {

    /**
     * 查询用户消息
     *
     * @param id 用户消息主键
     * @return 用户消息
     */
    NewsUserInfoVo queryById(Long id);

    /**
     * 查询用户消息列表
     *
     * @param bo 用户消息
     * @return 用户消息集合
     */
    TableDataInfo<NewsUserInfoVo> queryPageList(NewsUserInfoBo bo, PageQuery pageQuery);

    /**
     * 查询用户消息列表
     *
     * @param bo 用户消息
     * @return 用户消息集合
     */
    List<NewsUserInfoVo> queryList(NewsUserInfoBo bo);

    /**
     * 修改用户消息
     *
     * @param bo 用户消息
     * @return 结果
     */
    Boolean insertByBo(NewsUserInfoBo bo);

    /**
     * 修改用户消息
     *
     * @param bo 用户消息
     * @return 结果
     */
    Boolean updateByBo(NewsUserInfoBo bo);

    /**
     * 校验并批量删除用户消息信息
     *
     * @param ids     需要删除的用户消息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<NewsUserSumVo> listSum();

    List<NewsUserSumVo> listSums(String uid);

    Object userList(NewsUserInfoBo bo, PageQuery pageQuery);

    List<NewsUserSumVo> pushOne(String userid);

    boolean newsRead(NewsUserRecord bo);

    List<NewsUserSumVo> listInfo();

}

