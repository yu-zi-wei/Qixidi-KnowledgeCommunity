package com.qixidi.business.api.frontDesk;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.service.news.INewsUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 【前台】用户消息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/news")
public class FdNewsController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息汇总
     */
    @GetMapping("/list/sum")
    public List<NewsUserSumVo> listSum() {
        return iNewsUserInfoService.listSum();
    }

    @GetMapping("/list/info")
    public List<NewsUserSumVo> listInfo() {
        return iNewsUserInfoService.listInfo();
    }

    /**
     * 评论消息列表
     */
    @GetMapping("/comment/list")
    public TableDataInfo<ArticleCommentNewsVo> commentList(PageQuery pageQuery) {
        return iNewsUserInfoService.commentList(pageQuery);
    }

    /**
     * 点赞消息列表
     */
    @GetMapping("/fabulous/list")
    public TableDataInfo<NewsUserInfoVo> fabulousList(PageQuery pageQuery) {
        return iNewsUserInfoService.fabulousList(pageQuery);
    }

    /**
     * 关注消息列表
     */
    @GetMapping("/follow/list")
    public TableDataInfo<NewsUserInfoVo> followList(PageQuery pageQuery) {
        return iNewsUserInfoService.followList(pageQuery);
    }

    /**
     * 系统消息列表
     */
    @GetMapping("/system/list")
    public TableDataInfo<NewsUserInfoVo> systemList(PageQuery pageQuery) {
        return iNewsUserInfoService.systemList(pageQuery);
    }

    /**
     * 新增消息
     */
    @Log(title = "用户消息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody NewsUserInfoBo bo) {
        iNewsUserInfoService.insertByBo(bo);
    }

    /**
     * 消息已读
     */
    @Log(title = "用户消息", businessType = BusinessType.UPDATE)
    @GetMapping("/news-read")
    public void newsRead(NewsUserRecord bo) {
        iNewsUserInfoService.newsRead(bo);
    }

    /**
     * 删除消息
     */
    @Log(title = "用户消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iNewsUserInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
