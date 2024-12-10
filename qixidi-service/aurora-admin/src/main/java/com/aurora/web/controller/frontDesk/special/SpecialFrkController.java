package com.aurora.web.controller.frontDesk.special;

import com.aurora.business.domain.bo.special.SpecialInformationBo;
import com.aurora.business.domain.vo.special.SpecialInformationVo;
import com.aurora.business.service.special.ISpecialInformationService;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 【前台-白名单】专栏信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class SpecialFrkController extends BaseController {

    private final ISpecialInformationService iSpecialInformationService;

    /**
     * 查询专栏信息列表
     */
    @GetMapping("/special/list")
    public R specialListUid(SpecialInformationBo bo) {
        return R.ok(iSpecialInformationService.specialListUid(bo));
    }

    /**
     * 查询专栏信息列表
     *
     * @return
     */
    @GetMapping("/aut/special/list")
    public R specialList() {
        return R.ok(iSpecialInformationService.specialList());
    }

    /**
     * 查询用户专栏文章
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/select/special")
    public R selectSpecial(SpecialInformationBo bo, PageQuery pageQuery) {
        return R.ok(iSpecialInformationService.selectSpecial(bo, pageQuery));
    }

    /**
     * 修改文章专栏
     *
     * @param ids
     * @param id
     * @param uid
     * @return
     */
    @PostMapping("/update/special/{id}/{uid}")
    public R updateSpecial(@RequestBody List<String> ids,
                           @NotNull(message = "专栏id不能为空") @PathVariable("id") Long id,
                           @NotNull(message = "用户id不能为空") @PathVariable("uid") String uid) {
        return toAjax(iSpecialInformationService.updateSpecial(id, uid, ids));
    }

    /**
     * 获取专栏信息详细信息
     */
    @GetMapping("/special/{id}")
    public R<SpecialInformationVo> getInfo(@ApiParam("主键")
                                           @NotNull(message = "专栏id不能为空")
                                           @PathVariable("id") Long id) {
        return R.ok(iSpecialInformationService.queryById(id));
    }

    /**
     * 删除专栏信息
     */
    @DeleteMapping("/delete/special/{id}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotNull(message = "主键不能为空") @PathVariable Long id) throws Exception {
        return iSpecialInformationService.remove(id, true);
    }
}
