package com.aurora.business.mapper.article;

import com.aurora.business.domain.bo.article.ArticleInformationBo;
import com.aurora.business.domain.bo.article.SortTypeBo;
import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.vo.CountUserWebsiteVo;
import com.aurora.business.domain.vo.article.ArticleCensusVo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.common.core.domain.CensusEntity;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.vo.CensusVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 文章信息Mapper接口
 *
 * @author aurora
 * @date 2022-08-16
 */
@Mapper
public interface ArticleInformationMapper extends BaseMapperPlus<ArticleInformationMapper, ArticleInformation, ArticleInformationVo> {

    IPage<ArticleInformationVo> selectIndex(Page<ArticleInformation> build, @Param(Constants.WRAPPER) QueryWrapper<ArticleInformation> wrapper);

    IPage<ArticleInformationVo> selectTypeSort(@Param("bo") SortTypeBo bo, Page<ArticleInformation> build);

    IPage<ArticleInformationVo> articleList(@Param("bo") ArticleInformationBo bo, Page<ArticleInformation> build);

    ArticleInformationVo details(@Param("id") Long id);

    List<ArticleInformationVo> relatedList(@Param("bo") ArticleInformationBo bo, @Param("pageQuery") PageQuery pageQuery);

    @Update("update article_information set like_times = like_times+1 where id=#{id}")
    Integer updateLikeTimes(Long id);

    @Select("")
    Long selectByUid(Long id);

    @Select("select id,user_id,article_title,audit_state,update_time from article_information where id=#{id}")
    ArticleInformationVo basicInfo(@Param("id") Long id);

    Page<ArticleInformationVo> getArticleInfo(@Param("bo") ArticleInformationBo bo, Page<ArticleInformation> build);

    Integer updateAdd(@Param("id") Long articleId, @Param("code") Integer code);

//    Integer updateAddList(@Param("ids") List<Long> articleId, @Param("code") Integer code);

    Integer updateDelete(@Param("id") Long articleId, @Param("code") Integer code);

//    Integer updateDeleteList(@Param("ids") List<Long> articleId, @Param("code") Integer code);

    ArticleInformationVo selectVoByIds(@Param("id") Long id);

    @Select("select id,user_id,article_title,update_time from article_information where user_id=#{bo.userId} and state=0 ")
    List<ArticleInformationVo> selectTime(@Param("bo") ArticleInformationBo bo);

    int updateSpecial(@Param("ids") List<String> ids, @Param("id") Long id);

    @Select("select id,user_id,article_title,update_time from article_information " +
        "where user_id=#{uid} and special_id=#{id} and state=0")
    List<ArticleInformationVo> selectSpecial(@Param("id") Long id, @Param("uid") String uid);

    Integer updateDeleteNumber(@Param("articleId") Long articleId, @Param("code") Integer code, @Param("size") int size);

    List<ArticleInformationVo> selected();

    List<CensusVo> selectCensusMonth(@Param("bo") CensusEntity bo);

    List<CensusVo> selectCensusYear(@Param("bo") CensusEntity bo);

    List<ArticleCensusVo> articleCensus(@Param("bo") CensusEntity bo);

    List<ArticleInformationVo> selectData(@Param("id") Long id, @Param("size") Long size);

    Integer updateListHeatWeight(@Param("list") List<ArticleInformation> heatWeightList);

    List<CountUserWebsiteVo> selectUserArticleTask();

    @Select("select audit_state,is_public from article_information where id=#{id} and state=0")
    ArticleInformationVo selectAuditStatus(@Param("id") Long id);

    Page<ArticleInformationVo> FollowArticleInfoList(@Param("bo") SortTypeBo bo, Page<ArticleInformation> build);

    Page<ArticleInformationVo> LabelGArticleInfoList(@Param("bo") SortTypeBo bo, Page<ArticleInformation> build);

    @Select("select label_id from article_information where audit_state=2 and state=0 and label_id is not null and label_id!=''")
    List<String> selectLabel();

    IPage<ArticleInformationVo> articleRecommendList(@Param("bo") ArticleInformationBo bo,@Param("labelResult") String labelResult, Page<Object> build);

    List<CensusVo> submissionCensus(@Param("uuid")String uuid, @Param("time")String time);

    List<ArticleInformationVo> latelyArticleList(@Param("bo") ArticleInformationBo bo, Page<ArticleInformation> build);
}


