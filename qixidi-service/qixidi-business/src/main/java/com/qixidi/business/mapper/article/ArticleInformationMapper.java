package com.qixidi.business.mapper.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.SortTypeBo;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("select id,user_id,article_title,audit_state,update_time from b_article_information where id=#{id}")
    ArticleInformationVo basicInfo(@Param("id") Long id);

    /**
     * 昵称搜索：nicknameUids 为 Service 查用户域（nickname like）转出的 uuid 集合，bo.nickname 非空时必传
     */
    Page<ArticleInformationVo> getArticleInfo(@Param("bo") ArticleInformationBo bo,
                                              @Param("nicknameUids") List<String> nicknameUids,
                                              Page<ArticleInformation> build);

    Integer updateAdd(@Param("id") Long articleId, @Param("code") Integer code);

    ArticleInformationVo selectVoByIds(@Param("id") Long id);

    @Select("select id,user_id,article_title,update_time from b_article_information where user_id=#{bo.userId} and state=0 ")
    List<ArticleInformationVo> selectTime(@Param("bo") ArticleInformationBo bo);

    int updateSpecial(@Param("ids") List<String> ids, @Param("id") Long id);

    @Select("select id,user_id,article_title,update_time from b_article_information " +
            "where user_id=#{uid} and special_id=#{id} and state=0")
    List<ArticleInformationVo> selectSpecial(@Param("id") Long id, @Param("uid") String uid);

    List<ArticleInformationVo> selected();

    List<CensusVo> selectCensusMonth(@Param("bo") CensusEntity bo);

    List<CensusVo> selectCensusYear(@Param("bo") CensusEntity bo);

    List<ArticleInformationVo> selectData(@Param("id") Long id, @Param("size") Long size);

    Integer updateListHeatWeight(@Param("list") List<ArticleInformation> heatWeightList);

    List<CountUserWebsiteVo> selectArticleCountByUserIds(@Param("uuids") java.util.Collection<String> uuids);

    List<CountUserWebsiteVo> selectViewCountByUserIds(@Param("uuids") java.util.Collection<String> uuids);

    /**
     * 关注文章流（authorUids=关注且存活的作者集合，Service 求交传入，等价原 join b_user_follow/b_user_main 过滤；
     * 作者展示字段由 Service 组装）
     */
    Page<ArticleInformationVo> FollowArticleInfoList(@Param("bo") SortTypeBo bo,
                                                     @Param("authorUids") List<String> authorUids,
                                                     Page<ArticleInformation> build);

    /**
     * 分组文章流（aliveUids=存活作者集合，等价原 join um.state=0 过滤；作者展示字段由 Service 组装）
     */
    Page<ArticleInformationVo> LabelGArticleInfoList(@Param("bo") SortTypeBo bo,
                                                     @Param("aliveUids") List<String> aliveUids,
                                                     Page<ArticleInformation> build);

    @Select("select label_id from b_article_information where audit_state=2 and state=0 and platform_type=1 and label_id is not null and label_id!=''")
    List<String> selectLabel();

    IPage<ArticleInformationVo> articleRecommendList(@Param("bo") ArticleInformationBo bo, @Param("labelResult") String labelResult, Page<Object> build);

    List<CensusVo> submissionCensus(@Param("uuid") String uuid, @Param("time") String time);

    Page<ArticleInformationVo> latelyArticleList(@Param("bo") ArticleInformationBo bo, @Param("page") Page<ArticleInformation> build);
}


