package com.aurora.web.controller.frontDesk;

import com.aurora.business.domain.bo.SearchRecordsBo;
import com.aurora.business.domain.vo.SearchRecordsVo;
import com.aurora.business.service.ISearchRecordsService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "搜索记录控制器", tags = {"搜索记录管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/search/records")
public class FdSearchRecordsController {

    private final ISearchRecordsService iSearchRecordsService;

    /**
     * 查询搜索记录列表
     */
    @ApiOperation("查询搜索记录列表")
    @GetMapping("/list")
    public TableDataInfo<SearchRecordsVo> list(@Validated(QueryGroup.class) SearchRecordsBo bo, PageQuery pageQuery) {
        return iSearchRecordsService.frontDeskList(bo, pageQuery);
    }

}
