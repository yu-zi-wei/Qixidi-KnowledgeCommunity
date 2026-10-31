package com.qixidi.business.api.dictum;

import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.dictum.DictumCommentBo;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.qixidi.business.service.dictum.DictumCommentService;
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
@LogModule("随笔评论")
public class DictumCommentController {

    @Autowired
    private DictumCommentService dictumCommentService;

    /**
     * 新增评论
     *
     * @param bo 评论信息
     * @return 新增后的评论（含真实 id）
     */
    @Log(title = "新增随笔评论", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public DictumCommentVo add(@Validated @RequestBody DictumCommentBo bo) {
        return dictumCommentService.add(bo);
    }

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    @Log(title = "删除随笔评论", businessType = BusinessType.DELETE)
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        dictumCommentService.delete(id);
    }
}
