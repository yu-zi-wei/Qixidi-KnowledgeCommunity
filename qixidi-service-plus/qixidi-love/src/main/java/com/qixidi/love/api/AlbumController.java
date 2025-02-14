/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:54
 * @version 1.0
 */
package com.qixidi.love.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.qixidi.love.domain.bo.AlbumBo;
import com.qixidi.love.domain.vo.AlbumVo;
import com.qixidi.love.service.IAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 【爱情小站】时光相册
 *
 * @author ziwei
 * @date 2022-11-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/album")
public class AlbumController extends BaseController {

    private final IAlbumService iAlbumService;

    /**
     * 查询时光相册列表
     */
    @SaCheckPermission("lover:album:list")
    @GetMapping("/list")
    public TableDataInfo<AlbumVo> list(AlbumBo bo, PageQuery pageQuery) {
        return iAlbumService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询时光相册列表
     */
    @GetMapping("/api")
    public TableDataInfo<AlbumVo> listApi(AlbumBo bo, PageQuery pageQuery) {
        return iAlbumService.queryPageList(bo, pageQuery);
    }


    /**
     * 导出时光相册列表
     */
    @SaCheckPermission("lover:album:export")
    @Log(title = "时光相册", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(AlbumBo bo, HttpServletResponse response) {
        List<AlbumVo> list = iAlbumService.queryList(bo);
        ExcelUtil.exportExcel(list, "时光相册", AlbumVo.class, response);
    }

    /**
     * 获取时光相册详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:album:query")
    @GetMapping("/{id}")
    public R<AlbumVo> getInfo(@NotNull(message = "主键不能为空")
                              @PathVariable Long id) {
        return R.ok(iAlbumService.queryById(id));
    }

    /**
     * 新增时光相册
     */
    @SaCheckPermission("lover:album:add")
    @Log(title = "时光相册", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AlbumBo bo) {
        return toAjax(iAlbumService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改时光相册
     */
    @SaCheckPermission("lover:album:edit")
    @Log(title = "时光相册", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AlbumBo bo) {
        return toAjax(iAlbumService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除时光相册
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:album:remove")
    @Log(title = "时光相册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAlbumService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
