package com.qixidi.business.api.backstage.stat;

import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.service.stat.IStatDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站数据统计
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/stat/data")
public class StatDataInfoController {

    private final IStatDataInfoService iStatDataInfoService;

    /**
     * 查询网站日数据
     */
    @GetMapping("/info")
    public StatDataInfoVo list(StatDataInfoVo bo) {
        return iStatDataInfoService.queryPageList(bo);
    }

    /**
     * 查询网站月统计数据
     */
    @GetMapping("/the/info")
    public Object theList(StatDataInfoVo bo) {
        return iStatDataInfoService.theList(bo);
    }

    /**
     * 查询网站标签/分类统计
     */
    @GetMapping("/label/data")
    public Object labelDate(StatDataInfoVo bo) {
        return iStatDataInfoService.labelDate(bo);
    }
}
