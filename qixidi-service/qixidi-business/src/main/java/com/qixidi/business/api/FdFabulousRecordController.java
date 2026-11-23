package com.qixidi.business.api;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.service.fabulous.IFabulousRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】点赞管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/fabulous")
@LogModule("点赞")
public class FdFabulousRecordController {

    private final IFabulousRecordService iFabulousRecordService;

    /**
     * 点赞（type：1=文章，2=小记）
     */
    @Log(title = "点赞内容", businessType = BusinessType.INSERT)
    @PostMapping("/spot")
    public Integer spotFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        iFabulousRecordService.spotFabulous(bo);
        return 1;
    }

    /**
     * 取消点赞
     */
    @Log(title = "取消点赞", businessType = BusinessType.DELETE)
    @PostMapping("/cancel")
    public Integer cancelFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        iFabulousRecordService.cancelFabulous(bo);
        return 1;
    }

    /**
     * 用户点赞文章列表
     */
    @PostMapping("/fabulous/articleList")
    public TableDataInfo<ArticleInformationVo> fabulousArticleList(@Validated @RequestBody UserHomeBo bo) {
        return iFabulousRecordService.fabulousList(bo, bo);
    }

    /**
     * 用户点赞小记列表
     */
    @PostMapping("/fabulous/timeNotesList")
    public TableDataInfo<TimeNotesInfoVo> fabulousTimeNotesList(@Validated @RequestBody UserHomeBo bo) {
        return iFabulousRecordService.fabulousTimeNotesList(bo, bo);
    }

    /**
     * 用户点赞随笔列表
     */
    @PostMapping("/fabulous/dictumList")
    public TableDataInfo<DictumInfoVo> fabulousDictumList(@Validated @RequestBody UserHomeBo bo) {
        return iFabulousRecordService.fabulousDictumList(bo, bo);
    }

}
