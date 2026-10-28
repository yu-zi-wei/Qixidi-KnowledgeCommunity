package com.qixidi.mcp.tools;

import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.service.TimeNotesService;
import com.qixidi.mcp.domain.vo.timeNotes.McpTimeNoteSubmitVo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * 小记 tool：复刻前台时光小记链路（add/update），service 层自带角色与属主校验
 *
 * @author zi-wei
 */
@Component
public class TimeNotesTool {

    /**
     * @Lazy 断循环依赖：与 ArticleTools 同款防御（业务 Service 链可能经 ChatClient 卷入
     * Spring AI 自动装配环），见 ArticleTools 构造器注释
     */
    private final TimeNotesService timeNotesService;

    public TimeNotesTool(@Lazy TimeNotesService timeNotesService) {
        this.timeNotesService = timeNotesService;
    }

    @Tool(name = "create_time_note", description = "创建一条小记（时光小记），返回 timeNoteId。recordTime 缺省为今天。")
    public McpTimeNoteSubmitVo createTimeNote(
        @ToolParam(description = "小记标题，必填") String title,
        @ToolParam(description = "小记正文（Markdown）") String content,
        @ToolParam(description = "记录日期，格式 yyyy-MM-dd，缺省为今天", required = false) String recordTime
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 前台 @NotBlank 同语义（TimeNotesBo.title），MCP 直调 Service 须自行兜底
        if (title == null || title.isBlank()) {
            throw new ServiceException("title 不能为空");
        }
        TimeNotesBo bo = new TimeNotesBo();
        bo.setTitle(title);
        bo.setContent(content);
        bo.setRecordTime(parseOrToday(recordTime));
        // uid 由 service 层强制取当前登录用户，tool 层不传
        Long id = timeNotesService.add(bo);
        return submitVo(id, "created");
    }

    @Tool(name = "update_time_note", description = "更新自己的小记，仅传需修改的字段（未传保持不变）。仅限本人小记。")
    public McpTimeNoteSubmitVo updateTimeNote(
        @ToolParam(description = "小记 id（来自 create_time_note 返回值）") Long timeNoteId,
        @ToolParam(description = "新标题（不修改则不传）", required = false) String title,
        @ToolParam(description = "新正文（不修改则不传）", required = false) String content,
        @ToolParam(description = "记录日期 yyyy-MM-dd（不修改则不传）", required = false) String recordTime
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        TimeNotesBo bo = new TimeNotesBo();
        bo.setId(timeNoteId);
        bo.setTitle(title);
        bo.setContent(content);
        if (recordTime != null && !recordTime.isBlank()) {
            bo.setRecordTime(parseOrToday(recordTime));
        }
        // service 层已做角色与属主校验（Task 5），非本人小记抛"无权操作他人内容"
        timeNotesService.update(bo);
        return submitVo(timeNoteId, "updated");
    }

    private McpTimeNoteSubmitVo submitVo(Long timeNoteId, String status) {
        McpTimeNoteSubmitVo vo = new McpTimeNoteSubmitVo();
        vo.setTimeNoteId(timeNoteId);
        vo.setStatus(status);
        return vo;
    }

    private LocalDate parseOrToday(String recordTime) {
        if (recordTime == null || recordTime.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(recordTime.trim());
        } catch (DateTimeParseException e) {
            throw new ServiceException("recordTime 格式须为 yyyy-MM-dd");
        }
    }
}
