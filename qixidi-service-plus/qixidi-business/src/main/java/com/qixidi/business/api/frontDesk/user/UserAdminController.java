package com.qixidi.business.api.frontDesk.user;

import com.qixidi.business.service.article.IArticleInformationService;
import com.qixidi.business.service.collection.ICollectionInformationService;
import com.qixidi.business.service.special.ISpecialInformationService;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R timeArticleCensus(CensusEntity bo) {
        return R.ok(iArticleInformationService.timeArticleCensus(bo));
    }

    /**
     * 收藏夹统计
     *
     * @param bo
     */
    @GetMapping("/collection")
    public R timeCollectionCensus(CensusEntity bo) {
        return R.ok(iCollectionInformationService.timeCollectionCensus(bo));
    }

    /**
     * 专栏夹统计
     *
     * @param bo
     */
    @GetMapping("/special")
    public R timeSpecialCensus(CensusEntity bo) {
        return R.ok(iSpecialInformationService.timeSpecialCensus(bo));
    }

    /**
     * 发布文章次数统计
     *
     * @return
     */
    @GetMapping("/submission")
    public R submissionCensus() {
        return R.ok(iSpecialInformationService.submissionCensus());
    }

    /**
     * 用户基本数据信息
     *
     * @param bo
     * @return
     */
    @GetMapping("/count/user")
    public R CountUserCensus(CensusEntity bo) {
        return R.ok(iSpecialInformationService.CountUserCensus(bo));
    }

}
