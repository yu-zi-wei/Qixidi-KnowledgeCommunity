package com.aurora.web.controller.frontDesk.dictumWhite;

import com.aurora.business.domain.bo.dictum.DictumAlbumBo;
import com.aurora.business.domain.bo.dictum.DictumGroupBo;
import com.aurora.business.domain.bo.dictum.DictumInfoBo;
import com.aurora.business.domain.vo.dictum.DictumAlbumVo;
import com.aurora.business.domain.vo.dictum.DictumGroupVo;
import com.aurora.business.domain.vo.dictum.DictumInfoVo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.service.dictum.IDictumAlbumService;
import com.aurora.business.service.dictum.IDictumGroupService;
import com.aurora.business.service.dictum.IDictumInfoService;
import com.aurora.business.service.label.ILabelInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.RedisKeyEnums;
import com.aurora.common.utils.redis.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Api(value = "名言信息控制器开放接口", tags = {"名言信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/dictum")
public class WhDictumInfoController {

    private final IDictumInfoService iDictumInfoService;
    private final IDictumGroupService iDictumGroupService;
    private final IDictumAlbumService iDictumAlbumService;
    private final ILabelInfoService iLabelInfoService;
    /**
     * 查询名言信息列表（公开）
     */
    @ApiOperation("查询名言信息列表（公开）")
    @GetMapping("/info/list")
    public TableDataInfo<DictumInfoVo> list(@Validated(QueryGroup.class) DictumInfoBo bo, PageQuery pageQuery) {
        return iDictumInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询名言专辑列表(公开)
     */
    @ApiOperation("查询名言专辑列表（公开）")
    @GetMapping("/album/list")
    public TableDataInfo<DictumAlbumVo> list(@Validated(QueryGroup.class) DictumAlbumBo bo, PageQuery pageQuery) {
        return iDictumAlbumService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询名言分组列表(公开)
     */
    @ApiOperation("查询名言分组列表(公开)")
    @GetMapping("/group/list")
    public TableDataInfo<DictumGroupVo> list(@Validated(QueryGroup.class) DictumGroupBo bo, PageQuery pageQuery) {
        return iDictumGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取名言专辑详细信息
     */
    @ApiOperation("获取名言专辑详细信息")
    @GetMapping("/album/{id}")
    public R<DictumAlbumVo> getAlbumInfo(@ApiParam("主键")
                                         @NotNull(message = "主键不能为空")
                                         @PathVariable("id") Long id) {
        return R.ok(iDictumAlbumService.queryById(id));
    }

    /**
     * 推荐专辑
     */
    @ApiOperation("推荐专辑")
    @GetMapping("/recommended/album")
    public R recommendedAlbum() {
        return R.ok(iDictumAlbumService.recommendedAlbum());
    }

    /**
     * 热门作者
     */
    @ApiOperation("热门作者")
    @GetMapping("/popular/authors")
    public R<Set> popularAuthors() {
        Boolean aBoolean = RedisUtils.hasKey(RedisKeyEnums.POPULAR_AUTHORS.getKey());
        if (!aBoolean) R.ok(null);
        Set<Object> cacheSet = RedisUtils.getCacheSet(RedisKeyEnums.POPULAR_AUTHORS.getKey());
        return R.ok(cacheSet);
    }

    /**
     * 名言 热门标签
     */
    @ApiOperation("名言 热门标签")
    @GetMapping("/popular/label")
    public R<Set> popularLabel() {
        Boolean aBoolean = RedisUtils.hasKey(RedisKeyEnums.POPULAR_LABEL.getKey());
        if (!aBoolean) R.ok(null);
        Set<String> cacheSet = RedisUtils.getCacheSet(RedisKeyEnums.POPULAR_LABEL.getKey());
        return R.ok(cacheSet);
    }
   /**
     * 系统标签
     */
    @ApiOperation("系统标签")
    @GetMapping("/system/label")
    public R systemLabel(String label) {
        List<LabelInfoVo> list = iLabelInfoService.systemLabel(label);
        return R.ok(list);
    }

}
