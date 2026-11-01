package com.qixidi.system.api.news;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.news.NewsUserRecordBo;
import com.qixidi.business.domain.vo.news.NewsUserRecordVo;
import com.qixidi.business.service.news.INewsUserInfoService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 用户消息管理
 *
 * @author aurora
 * @date 2022-11-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/news")
@LogModule("用户消息")
public class NewsUserInfoController {

    private final INewsUserInfoService iNewsUserInfoService;

    /**
     * 查询用户消息记录列表（b_news_user_record，发送者/接收者昵称头像 + 类型/用户名筛选）
     */
    @SaCheckPermission("news:userInfo:list")
    @PostMapping("/record/list")
    public TableDataInfo<NewsUserRecordVo> recordList(@RequestBody NewsUserRecordBo bo) {
        return iNewsUserInfoService.recordPage(bo, bo);
    }

    /**
     * 删除用户消息记录
     */
    @SaCheckPermission("news:userInfo:remove")
    @Log(title = "删除用户消息记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/record/{ids}")
    public void removeRecord(@PathVariable Long[] ids) {
        iNewsUserInfoService.deleteRecordByIds(Arrays.asList(ids));
    }
}
