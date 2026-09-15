package com.qixidi.business.domain.bo.dictum;

import com.light.core.core.validate.AddGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


/**
 * 随笔批量发布业务对象（一次发布多条，分类/作者等元信息共享）
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Data
public class DictumBatchBo {

    /**
     * 随笔内容列表（空白内容由 service 过滤，单次最多 10 条）
     */
    @NotEmpty(message = "随笔内容不能为空", groups = {AddGroup.class})
    @Size(max = 10, message = "单次最多发布 10 条随笔", groups = {AddGroup.class})
    private List<String> contents;

    /**
     * 分类id
     */
    @NotNull(message = "分类不能为空", groups = {AddGroup.class})
    private Long groupId;

    /**
     * 专辑id
     */
    private Long albumId;

    /**
     * 标签（多个以逗号隔开）
     */
    private String label;

    /**
     * 图片（多个以逗号隔开）
     */
    private String picture;

    /**
     * 作者
     */
    private String author;

    /**
     * 作品名称
     */
    private String worksName;

    /**
     * 名言状态（1：公开，2：私有，3：关注可看）
     */
    @NotNull(message = "名言状态不能为空", groups = {AddGroup.class})
    private Integer dictumState;

}
