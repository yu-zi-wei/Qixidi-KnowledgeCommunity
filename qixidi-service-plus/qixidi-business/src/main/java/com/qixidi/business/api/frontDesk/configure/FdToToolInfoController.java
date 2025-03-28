package com.qixidi.business.api.frontDesk.configure;

import com.qixidi.business.domain.bo.configure.ToToolInfoBo;
import com.qixidi.business.domain.vo.configure.ToToolInfoVo;
import com.qixidi.business.service.configure.IToToolInfoService;
import com.light.core.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【前台】工具信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/white/configure/tool")
public class FdToToolInfoController {
    private final IToToolInfoService iToToolInfoService;

    /**
     * 查询父级工具信息列表
     */
    @GetMapping("/list")
    public List<ToToolInfoVo> fdList() {
        return iToToolInfoService.fdList();
    }

    /**
     * 查询子级工具信息列表
     */
    @GetMapping("/child/list")
    public List<ToToolInfoVo> childList(@Validated(QueryGroup.class) ToToolInfoBo bo) {
        return iToToolInfoService.childList(bo);
    }

}
