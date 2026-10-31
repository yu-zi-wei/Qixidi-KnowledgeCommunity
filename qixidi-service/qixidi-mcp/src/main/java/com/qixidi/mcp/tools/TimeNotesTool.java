package com.qixidi.mcp.tools;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.auth.helper.OperLogRecorder;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.service.TimeNotesService;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.mcp.domain.bo.timeNotes.McpTimeNoteQueryBo;
import com.qixidi.mcp.domain.vo.timeNotes.McpTimeNoteItemVo;
import com.qixidi.mcp.domain.vo.timeNotes.McpTimeNotePageVo;
import com.qixidi.mcp.domain.vo.timeNotes.McpTimeNoteSubmitVo;
import com.qixidi.mcp.utils.McpPageUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

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
    private final OperLogRecorder operLogRecorder;

    public TimeNotesTool(@Lazy TimeNotesService timeNotesService, OperLogRecorder operLogRecorder) {
        this.timeNotesService = timeNotesService;
        this.operLogRecorder = operLogRecorder;
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
        operLogRecorder.record("MCP小记", "创建小记", BusinessType.INSERT,
            user.getUsername(), "id=" + id + ", title=" + title, null);
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
        operLogRecorder.record("MCP小记", "更新小记", BusinessType.UPDATE,
            user.getUsername(), "id=" + timeNoteId + ", title=" + title, null);
        return submitVo(timeNoteId, "updated");
    }

    @Tool(name = "search_time_notes", description = "多条件搜索本人的小记：标题/正文模糊、记录日期范围。分页返回 timeNoteId/title/recordTime/content（含正文），单条详情可用 get_time_note。仅限本人小记。")
    public McpTimeNotePageVo searchTimeNotes(
        @ToolParam(description = "查询条件对象") McpTimeNoteQueryBo query,
        @ToolParam(description = "页码，从 1 开始，默认 1", required = false) Integer pageNum,
        @ToolParam(description = "每页条数，默认 100，最大 500", required = false) Integer pageSize
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        Page<TimeNotes> page = new Page<>(McpPageUtils.pageNum(pageNum), McpPageUtils.pageSize(pageSize));
        Page<TimeNotes> result = timeNotesService.searchForAgent(user.getUuid(),
            query.getTitle(), query.getContent(),
            parseDate(query.getBeginDate()), parseDate(query.getEndDate()), page);
        List<McpTimeNoteItemVo> rows = result.getRecords().stream().map(n -> {
            McpTimeNoteItemVo row = new McpTimeNoteItemVo();
            row.setTimeNoteId(n.getId());
            row.setTitle(n.getTitle());
            row.setRecordTime(n.getRecordTime() == null ? null : n.getRecordTime().toString());
            row.setContent(n.getContent());
            return row;
        }).collect(Collectors.toList());
        McpTimeNotePageVo vo = new McpTimeNotePageVo();
        vo.setTotal(result.getTotal());
        vo.setRows(rows);
        return vo;
    }

    @Tool(name = "get_time_note", description = "获取指定小记的详情（标题/正文/记录日期），timeNoteId 来自 search_time_notes 或 create_time_note 返回值。")
    public McpTimeNoteItemVo getTimeNote(
        @ToolParam(description = "小记 id") Long timeNoteId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 复用前台公开详情链路 getInfo（小记为公开内容），不存在返回 null
        TimeNotesInfoVo vo = timeNotesService.getInfo(timeNoteId);
        if (vo == null) {
            throw new ServiceException("小记不存在");
        }
        McpTimeNoteItemVo item = new McpTimeNoteItemVo();
        item.setTimeNoteId(vo.getId());
        item.setTitle(vo.getTitle());
        item.setContent(vo.getContent());
        item.setRecordTime(vo.getRecordTime() == null ? null : vo.getRecordTime().toString());
        return item;
    }

    private McpTimeNoteSubmitVo submitVo(Long timeNoteId, String status) {
        McpTimeNoteSubmitVo vo = new McpTimeNoteSubmitVo();
        vo.setTimeNoteId(timeNoteId);
        vo.setStatus(status);
        return vo;
    }

    private LocalDate parseOrToday(String recordTime) {
        LocalDate parsed = parseDate(recordTime);
        return parsed == null ? LocalDate.now() : parsed;
    }

    /**
     * 日期入参解析：可空（范围查询空=无边界）；格式错误明确报错
     */
    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            throw new ServiceException("日期格式须为 yyyy-MM-dd：" + date);
        }
    }
}
