package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.service.article.IArticleInformationService;
import com.aurora.business.service.collection.ICollectionInformationService;
import com.aurora.business.service.special.ISpecialInformationService;
import com.aurora.common.core.domain.CensusEntity;
import com.aurora.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Api(value = "前端用户后台控制器", tags = {"前端用户后台控制器"})
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
    @ApiOperation("按时间统计用户文章")
    @GetMapping("/time/article")
    public R timeArticleCensus(CensusEntity bo) {
        return R.ok(iArticleInformationService.timeArticleCensus(bo));
    }

    /**
     * 收藏夹统计
     *
     * @param bo
     */
    @ApiOperation("收藏夹统计")
    @GetMapping("/collection")
    public R timeCollectionCensus(CensusEntity bo) {
        return R.ok(iCollectionInformationService.timeCollectionCensus(bo));
    }

    /**
     * 专栏夹统计
     *
     * @param bo
     */
    @ApiOperation("专栏夹统计")
    @GetMapping("/special")
    public R timeSpecialCensus(CensusEntity bo) {
        return R.ok(iSpecialInformationService.timeSpecialCensus(bo));
    }

    @ApiOperation("发布文章次数统计")
    @GetMapping("/submission")
    public R submissionCensus() {
        return R.ok(iSpecialInformationService.submissionCensus());
    }

    @ApiOperation("用户基本数据信息")
    @GetMapping("/count/user")
    public R CountUserCensus(CensusEntity bo) {
        return R.ok(iSpecialInformationService.CountUserCensus(bo));
    }

}
