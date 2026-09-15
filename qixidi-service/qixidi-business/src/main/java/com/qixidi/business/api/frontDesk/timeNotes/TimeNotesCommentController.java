package com.qixidi.business.api.frontDesk.timeNotes;

import com.qixidi.business.domain.bo.timeNotes.TimeNotesCommentBo;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;
import com.qixidi.business.service.timeNotes.TimeNotesCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 时光小记评论（需登录）
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/time/notes/comment")
public class TimeNotesCommentController {

    private final TimeNotesCommentService timeNotesCommentService;

    /**
     * 新增评论
     *
     * @param bo 评论信息
     * @return 新增后的评论（含真实 id）
     */
    @PostMapping("/add")
    public TimeNotesCommentVo add(@Validated @RequestBody TimeNotesCommentBo bo) {
        return timeNotesCommentService.add(bo);
    }

    /**
     * 删除评论（仅本人）
     *
     * @param id 评论 id
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        timeNotesCommentService.delete(id);
    }
}
