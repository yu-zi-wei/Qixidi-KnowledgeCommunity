package com.qixidi.system.api.stat;

import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.service.stat.IStatDataInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站数据统计
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/stat/data")
public class StatDataInfoController {

    private final IStatDataInfoService iStatDataInfoService;

    /**
     * 查询网站日数据
     */
    @PostMapping("/info")
    public StatDataInfoVo list(@RequestBody StatDataInfoVo bo) {
        return iStatDataInfoService.queryPageList(bo);
    }

    /**
     * 查询网站月统计数据
     */
    @PostMapping("/the/info")
    public Object theList(@RequestBody StatDataInfoVo bo) {
        return iStatDataInfoService.theList(bo);
    }

    /**
     * 查询网站标签/分类统计
     */
    @PostMapping("/label/data")
    public Object labelDate(@RequestBody StatDataInfoVo bo) {
        return iStatDataInfoService.labelDate(bo);
    }
}
