package com.aurora.web.controller.business.tool;

import com.aurora.business.service.tool.MysqlToolService;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Api(value = "mysql工具接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/tool/mysql")
public class MysqlToolController extends BaseController {

    @Autowired
    private MysqlToolService mysqlToolService;

    @ApiOperation("清空业务表数据")
    @Log(title = "清空业务表数据", businessType = BusinessType.UPDATE)
    @GetMapping("/emptyData")
    public R emptyData() {
        return toAjax(mysqlToolService.emptyData() ? 1 : 0);
    }
}
