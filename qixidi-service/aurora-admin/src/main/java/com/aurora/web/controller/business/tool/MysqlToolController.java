package com.aurora.web.controller.business.tool;

import com.aurora.business.service.tool.MysqlToolService;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * mysql工具接口
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/tool/mysql")
public class MysqlToolController extends BaseController {

    @Autowired
    private MysqlToolService mysqlToolService;

    /**
     * 清空业务表数据
     *
     * @return
     */
    @Log(title = "清空业务表数据", businessType = BusinessType.UPDATE)
    @GetMapping("/emptyData")
    public R emptyData() {
        return toAjax(mysqlToolService.emptyData() ? 1 : 0);
    }
}
