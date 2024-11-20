package com.aurora.web.controller.business.stat;

import com.aurora.business.domain.vo.stat.StatDataInfoVo;
import com.aurora.business.service.stat.IStatDataInfoService;
import com.aurora.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Api(value = "网站数据统计", tags = {"网站数据统计"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/stat/data")
public class StatDataInfoController {

    private final IStatDataInfoService iStatDataInfoService;

    /**
     * 查询网站日数据
     */
    @ApiOperation("查询网站日数据")
    @GetMapping("/info")
    public R<StatDataInfoVo> list(StatDataInfoVo bo) {
        return R.ok(iStatDataInfoService.queryPageList(bo));
    }

    /**
     * 查询网站月统计数据
     */
    @ApiOperation("查询网站月统计数据")
    @GetMapping("/the/info")
    public R theList(StatDataInfoVo bo) {
        return R.ok(iStatDataInfoService.theList(bo));
    }

    /**
     * 查询网站标签/分类统计
     */
    @ApiOperation("查询网站标签/分类统计")
    @GetMapping("/label/data")
    public R labelDate(StatDataInfoVo bo) {
        return R.ok(iStatDataInfoService.labelDate(bo));
    }
}
