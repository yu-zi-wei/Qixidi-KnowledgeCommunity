package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.core.domain.R;
import com.aurora.system.service.ITripartiteUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Validated
@Api(value = "", tags = {""})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/user")
public class UserWebInfo {

    private final ITripartiteUserService iTripartiteUserService;

    @ApiOperation("获取用户网站记录信息")
    @PostMapping("/website/info")
    public R<TripartiteUserVo> websiteInfo() {
        return R.ok(iTripartiteUserService.websiteInfo());
    }

    @ApiOperation("通过用户id获取用户网站记录信息")
    @GetMapping("/website/info/{id}")
    public R<TripartiteUserVo> getWebsiteInfo(@ApiParam("主键")
                                              @NotNull(message = "主键不能为空")
                                              @PathVariable("uuid") String uuid) {
        return R.ok(iTripartiteUserService.getWebsiteInfo(uuid));
    }

}
