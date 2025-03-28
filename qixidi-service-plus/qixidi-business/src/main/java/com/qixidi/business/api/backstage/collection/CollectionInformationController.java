package com.qixidi.business.api.backstage.collection;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.collection.CollectionInformationBo;
import com.qixidi.business.domain.vo.collection.CollectionInformationVo;
import com.qixidi.business.service.collection.ICollectionInformationService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 收藏夹信息管理
 *
 * @author aurora
 * @date 2022-09-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/collection/information")
public class CollectionInformationController extends BaseController {

    private final ICollectionInformationService iCollectionInformationService;

    /**
     * 查询收藏夹信息列表
     */
    @SaCheckPermission("collection:information:list")
    @GetMapping("/list")
    public TableDataInfo<CollectionInformationVo> list(@Validated(QueryGroup.class) CollectionInformationBo bo, PageQuery pageQuery) {
        return iCollectionInformationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出收藏夹信息列表
     */
    @SaCheckPermission("collection:information:export")
    @Log(title = "收藏夹信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated CollectionInformationBo bo, HttpServletResponse response) {
        List<CollectionInformationVo> list = iCollectionInformationService.queryList(bo);
        ExcelUtil.exportExcel(list, "收藏夹信息", CollectionInformationVo.class, response);
    }

    /**
     * 获取收藏夹信息详细信息
     */
    @SaCheckPermission("collection:information:query")
    @GetMapping("/{id}")
    public R<CollectionInformationVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iCollectionInformationService.queryById(id));
    }

    /**
     * 新增收藏夹信息
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 更新收藏夹信息
     *
     * @param bo
     * @return
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除收藏夹信息
     *
     * @param ids
     * @return
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iCollectionInformationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

