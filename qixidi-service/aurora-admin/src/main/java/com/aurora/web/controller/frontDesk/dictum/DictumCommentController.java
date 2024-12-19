package com.aurora.web.controller.frontDesk.dictum;

import com.aurora.business.domain.bo.dictum.DictumCommentBo;
import com.aurora.business.service.dictum.DictumCommentService;
import com.aurora.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zi-wei
 * @create 2024/12/17 14:18
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/comment")
public class DictumCommentController {
    @Autowired
    private DictumCommentService dictumCommentService;

    /**
     * 新增评论
     *
     * @param bo
     * @return
     */
    @PostMapping("/add")
    public R<Void> add(@Validated @RequestBody DictumCommentBo bo) {
        dictumCommentService.add(bo);
        return R.ok();
    }

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        dictumCommentService.delete(id);
        return R.ok();
    }
}
