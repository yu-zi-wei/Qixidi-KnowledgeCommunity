package com.qixidi.business.comtroller.frontDesk;

import com.qixidi.business.domain.bo.SearchRecordsBo;
import com.qixidi.business.domain.vo.SearchRecordsVo;
import com.qixidi.business.service.ISearchRecordsService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【前台】搜索记录管理
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/search/records")
public class FdSearchRecordsController {

    private final ISearchRecordsService iSearchRecordsService;

    /**
     * 查询搜索记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<SearchRecordsVo> list(@Validated(QueryGroup.class) SearchRecordsBo bo, PageQuery pageQuery) {
        return iSearchRecordsService.frontDeskList(bo, pageQuery);
    }

}
