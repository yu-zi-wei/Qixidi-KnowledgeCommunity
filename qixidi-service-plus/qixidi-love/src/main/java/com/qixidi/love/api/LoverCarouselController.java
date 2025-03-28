/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:19
 * @version 1.0
 */
package com.qixidi.love.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.excel.utils.ExcelUtil;
import com.qixidi.love.domain.bo.LoverCarouselBo;
import com.qixidi.love.domain.vo.LoverCarouselVo;
import com.qixidi.love.service.ILoverCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;


/**
 * 【爱情小站】爱情轮播图
 *
 * @author ziwei
 * @date 2022-11-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lover/carousel")
public class LoverCarouselController extends BaseController {

    private final ILoverCarouselService iLoverCarouselService;

    /**
     * 查询爱情轮播图列表
     */
    @SaCheckPermission("lover:carousel:list")
    @GetMapping("/list")
    public TableDataInfo<LoverCarouselVo> list(LoverCarouselBo bo, PageQuery pageQuery) {
        return iLoverCarouselService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出爱情轮播图列表
     */
    @SaCheckPermission("lover:carousel:export")
    @PostMapping("/export")
    public void export(LoverCarouselBo bo, HttpServletResponse response) {
        List<LoverCarouselVo> list = iLoverCarouselService.queryList(bo);
        ExcelUtil.exportExcel(list, "爱情轮播图", LoverCarouselVo.class, response);
    }

    /**
     * 获取爱情轮播图详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("lover:carousel:query")
    @GetMapping("/{id}")
    public R<LoverCarouselVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable Long id) {
        return R.ok(iLoverCarouselService.queryById(id));
    }

    /**
     * 新增爱情轮播图
     */
    @SaCheckPermission("lover:carousel:add")
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LoverCarouselBo bo) {
        return toAjax(iLoverCarouselService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改爱情轮播图
     */
    @SaCheckPermission("lover:carousel:edit")
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LoverCarouselBo bo) {
        return toAjax(iLoverCarouselService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除爱情轮播图
     *
     * @param ids 主键串
     */
    @SaCheckPermission("lover:carousel:remove")
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLoverCarouselService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
