package com.qixidi.business.api.frontDesk.dictumWhite;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import com.light.redission.utils.RedisUtils;
import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.service.dictum.DictumCommentService;
import com.qixidi.business.service.dictum.IDictumAlbumService;
import com.qixidi.business.service.dictum.IDictumGroupService;
import com.qixidi.business.service.dictum.IDictumInfoService;
import com.qixidi.business.service.label.ILabelInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 【前台-白名单】名言信息管理
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/dictum")
public class WhDictumInfoController {

    private final IDictumInfoService iDictumInfoService;
    private final IDictumGroupService iDictumGroupService;
    private final IDictumAlbumService iDictumAlbumService;
    private final ILabelInfoService iLabelInfoService;
    @Autowired
    private DictumCommentService dictumCommentService;

    /**
     * 查询名言信息列表（公开）
     */
    @GetMapping("/info/list")
    public TableDataInfo<DictumInfoVo> list(@Validated(QueryGroup.class) DictumInfoBo bo, PageQuery pageQuery) {
        return iDictumInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取名言详情(公开)
     *
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public R<DictumInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumInfoService.queryById(id));
    }

    /**
     * 查询名言专辑列表(公开)
     */
    @GetMapping("/album/list")
    public TableDataInfo<DictumAlbumVo> list(@Validated(QueryGroup.class) DictumAlbumBo bo, PageQuery pageQuery) {
        return iDictumAlbumService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询名言分组列表(公开)
     */
    @GetMapping("/group/list")
    public TableDataInfo<DictumGroupVo> list(@Validated(QueryGroup.class) DictumGroupBo bo, PageQuery pageQuery) {
        return iDictumGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取名言专辑详细信息
     */
    @GetMapping("/album/{id}")
    public R<DictumAlbumVo> getAlbumInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumAlbumService.queryById(id));
    }

    /**
     * 推荐专辑
     */
    @GetMapping("/recommended/album")
    public R recommendedAlbum() {
        return R.ok(iDictumAlbumService.recommendedAlbum());
    }

    /**
     * 热门作者
     */
    @GetMapping("/popular/authors")
    public R<Set> popularAuthors() {
        Boolean aBoolean = RedisUtils.hasKey(RedisBusinessKeyEnums.POPULAR_AUTHORS.getKey());
        if (!aBoolean) R.ok(null);
        Set<Object> cacheSet = RedisUtils.getCacheSet(RedisBusinessKeyEnums.POPULAR_AUTHORS.getKey());
        return R.ok(cacheSet);
    }

    /**
     * 名言 热门标签
     */
    @GetMapping("/popular/label")
    public R<Set> popularLabel() {
        Boolean aBoolean = RedisUtils.hasKey(RedisBusinessKeyEnums.POPULAR_LABEL.getKey());
        if (!aBoolean) R.ok(null);
        Set<String> cacheSet = RedisUtils.getCacheSet(RedisBusinessKeyEnums.POPULAR_LABEL.getKey());
        return R.ok(cacheSet);
    }

    /**
     * 系统标签
     */
    @GetMapping("/system/label")
    public R systemLabel(String label) {
        List<LabelInfoVo> list = iLabelInfoService.systemLabel(label);
        return R.ok(list);
    }

    /**
     * 获取名言评论
     *
     * @param id
     * @param pageQuery
     * @return
     */
    @GetMapping("comment/list/{id}")
    public TableDataInfo<DictumCommentVo> commentList(@PathVariable("id") Long id, PageQuery pageQuery) {
        return dictumCommentService.commentList(id, pageQuery);
    }

}
