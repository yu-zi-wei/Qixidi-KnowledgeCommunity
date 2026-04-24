package com.qixidi.business.api.frontDesk.user;

import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.vo.CensusVo;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.service.article.IArticleInformationService;
import com.qixidi.business.service.collection.ICollectionInformationService;
import com.qixidi.business.service.special.ISpecialInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【前台】前端用户后台管理
 */
@Validated
@RequiredArgsConstructor
@RestController()
@RequestMapping("/user/census")
public class UserAdminController {
    private final IArticleInformationService iArticleInformationService;
    private final ICollectionInformationService iCollectionInformationService;
    private final ISpecialInformationService iSpecialInformationService;

    /**
     * 按时间统计用户文章
     *
     * @param bo
     */
    @GetMapping("/time/article")
    public List<CensusVo> timeArticleCensus(CensusEntity bo) {
        return iArticleInformationService.timeArticleCensus(bo);
    }

    /**
     * 收藏夹统计
     *
     * @param bo
     */
    @GetMapping("/collection")
    public List<CensusVo> timeCollectionCensus(CensusEntity bo) {
        return iCollectionInformationService.timeCollectionCensus(bo);
    }

    /**
     * 专栏夹统计
     *
     * @param bo
     */
    @GetMapping("/special")
    public List<CensusVo> timeSpecialCensus(CensusEntity bo) {
        return iSpecialInformationService.timeSpecialCensus(bo);
    }

    /**
     * 发布文章次数统计
     *
     * @return
     */
    @GetMapping("/submission")
    public List<CensusVo> submissionCensus() {
        return iSpecialInformationService.submissionCensus();
    }

    /**
     * 用户基本数据信息
     */
    @GetMapping("/count/user")
    public CountUserWebsiteVo CountUserCensus() {
        return iSpecialInformationService.CountUserCensus();
    }

}
